package mwcompiler.symbols;

import mwcompiler.ast.nodes.TypeNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionTypeSymbol extends TypeSymbol {
    private TypeSymbol returnType;
    private List<TypeSymbol> params;

    private FunctionTypeSymbol(String returnType, String... params) {
        this.returnType = NonArrayTypeSymbol.getSymbol(returnType);
        List<TypeSymbol> typeParams = new ArrayList<>();
        for (String param : params) {
            typeParams.add(NonArrayTypeSymbol.getSymbol(param));
        }
        this.params = typeParams;
    }

    private FunctionTypeSymbol(TypeSymbol returnType, List<TypeSymbol> params) {
        this.returnType = returnType;
        this.params = params;
    }


    public static FunctionTypeSymbol builder(TypeNode returnType, List<TypeNode> params) {
        List<TypeSymbol> typeParams = new ArrayList<>();
        TypeSymbol returnTypeSymbol;
        try {
            returnTypeSymbol = returnType.getSymbol();
        } catch (RuntimeException e) {
            throw new RuntimeException("Unknown function return typename " + returnType.getTypename());
        }

        for (Integer index = 0; index < params.size(); ++index) {
            TypeSymbol typeParam;
            try {
                typeParam = params.get(index).getSymbol();
            } catch (RuntimeException e) {
                throw new RuntimeException("Unknown parameter(" + String.valueOf(index) + ") typename " + params.get(index).getTypename());
            }
            typeParams.add(typeParam);
        }
        return new FunctionTypeSymbol(returnTypeSymbol, typeParams);
    }

    public TypeSymbol getReturnType() {
        return returnType;
    }

    public List<TypeSymbol> getParams() {
        return params;
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder("returnType: " + returnType.getName() + "\nParams:\n");
        for (TypeSymbol param : params) {
            name.append("\t").append(param.getName()).append("\n");
        }
        return String.valueOf(name);
    }
}
