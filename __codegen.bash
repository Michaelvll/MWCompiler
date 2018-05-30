set -e
cd "$(dirname "$0")"
cat > program.in
java -cp ./lib/*:./bin mwcompiler.Mwcc -i program.in
cat ./lib/lib.asm
