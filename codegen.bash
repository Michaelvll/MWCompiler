set -e
cd "$(dirname "$0")"
cat > program.in
java -cp ./lib/*:./bin mwcompiler.Mwcc -i program.in -a Graph --memorize-search
cat ./lib/lib.asm
