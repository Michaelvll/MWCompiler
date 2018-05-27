set -e
cd "$(dirname "$0")"
cat > program.in
java Mwcc -i program.in
