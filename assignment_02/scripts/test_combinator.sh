#!/bin/bash

cabal test || {
  echo "[ERROR] Failing test cases!"
  exit 1
}

cabal build combinator || {
  echo "[ERROR] Build failed!"
  exit 1
}

exe=$(find ./dist-newstyle -type f -name "combinator")

errors=0
failures=()

if [ -f "./inputs/inputs.zip" ]; then
  cd inputs
  unzip -n inputs.zip
  cd ../
fi

for valid in $(find ./inputs -type f -name "*.valid.mini"); do
  $exe $valid > /dev/null
  rc=$?
  if [ $rc -eq 0 ]; then
    echo "[INFO] Pass: $valid"
  else
    echo "[ERROR] Fail: $valid"
    errors=$((errors+1))
    failures+=($valid)
  fi
done

for invalid in $(find ./inputs -type f -name "*.invalid.mini"); do
  $exe $invalid > /dev/null
  rc=$?
  if [ $rc -eq 0 ]; then
    echo "[ERROR] Fail: $invalid"
    errors=$((errors+1))
    failures+=($invalid)
  else
    echo "[INFO] Pass: $invalid"
  fi
done

if ! [ $errors -eq 0 ]; then
  echo "[ERROR] There were unexpected failures!"
  for fail in "${failures[@]}"; do
    echo $fail
  done
  exit 1
fi
