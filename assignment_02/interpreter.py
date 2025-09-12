import operator

def tokenize(expression):
    return expression.replace('(', ' ( ').replace(')', ' ) ').split()

def parse(tokens):
    if not tokens:
        raise SyntaxError("Unexpected EOF")
    token = tokens.pop(0)
    if token == '(':
        expr = []
        while tokens[0] != ')':
            expr.append(parse(tokens))
        tokens.pop(0) # pop off ')'
        return expr
    elif token == ')':
        raise SyntaxError("Unexpected )")
    elif token.startswith("'"):
        return ['quote', parse([token[1:]])]
    else: 
        return atom(token)
    
def atom(token):
    try:
        return int(token)
    except ValueError:
        return str(token)
    
def evaluate():
    env = {
    'plus': lambda x, y: x + y,
    'minus': lambda x, y: x - y,
    'mult': lambda x, y: x * y,
    'div': lambda x, y: x // y,
    'eq': lambda x, y: x == y, #TODO: Check if this is correct with how we handle booleans
    'gt': lambda x, y: x > y,
    'car': lambda lst: lst[0] if lst else 'nil',
    'cdr': lambda lst: lst[1:] if lst else 'nil',
    'cons': lambda x, lst: [x] + lst if isinstance(lst, list) else [x, lst],
    'list': lambda *args: list(args),
    'nil': [],
    'true': True, 
    'false': False
    }
    return env

def eval_expr(expr, env):
    if isinstance(expr, int):  
        return expr
    elif isinstance(expr, str):  
        return env[expr]
    elif isinstance(expr, list): 
        if not expr:
            return []
        op = expr[0]
        if op == 'def':
            _, name, value = expr
            env[name] = eval_expr(value, env)
            return env[name]
        elif op == 'lamba':
            _, params, body = expr
            return lambda *args: eval_expr(body, dict(env, **dict(zip(params, args))))
        elif op == 'cond':
            _, test, then_expr, else_expr = expr
            condition = eval_expr(test, env)
            return eval_expr(then_expr, env) if condition != False else eval_expr(else_expr, env)
        elif op == 'quote':
            return expr[1]
        else:
                func = eval_expr(op, env)
                args = [eval_expr(arg, env) for arg in expr[1:]]
                return func(*args)
        
    else:
        raise TypeError(f"Unknown expression type: {expr}")
    
def parse_multiple(tokens):
    expressions = []
    while tokens:
        expressions.append(parse(tokens))
    return expressions   

    
def run(source, env=None):
    if env is None:
        env = evaluate()
    tokens = tokenize(source)
    parsed_expr = parse_multiple(tokens)
    result = None
    for expr in parsed_expr:
        result = eval_expr(expr, env)
    return result

if __name__ == "__main__":
    env = evaluate()
    buffer = []
    while True:
        try:
            line = input(">>> " if not buffer else "... ")           
            if line.lower() in ("exit", "quit"):
                break
            if line.strip() == "":  # empty line makes the program execute
                if buffer:
                    source = "\n".join(buffer)
                    result = run(source, env)
                    if result is not None:
                        print(result)
                    buffer = []  # reset buffer
                continue
            buffer.append(line)
        except Exception as e:
            print(f"Error: {e}")

