#!/usr/bin/env bash
# Run the tests for one exercise or debugging challenge.
#   ./run-task.sh BE-009        -> tests tagged BE-009
#   ./run-task.sh DBG-B02       -> one backend debugging challenge
#   ./run-task.sh todo          -> every exercise test (expect lots of failures at the start)
set -euo pipefail
if [ $# -ne 1 ]; then
  echo "usage: $0 <TAG>   e.g. $0 BE-001" >&2
  exit 1
fi
mvn test -Dgroups="$1" -DexcludedGroups=none -Dsurefire.failIfNoSpecifiedTests=false
