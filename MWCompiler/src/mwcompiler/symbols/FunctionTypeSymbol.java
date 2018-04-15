package mwcompiler.symbols;

import mwcompiler.ast.nodes.TypeNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionTypeSymbol extends TypeSymbol {
    private TypeSymbol returnType;
    private List<TypeSymbol> params;

    private FunctionTypeSymbol(String returnType, String... params) {
        this.returnType = NonArrayTypeSymbol.builder(returnType);
        List<TypeSymbol> typeParams = new ArrayList<>();
        for (String param : params) {
            typeParams.add(NonArrayTypeSymbol.builder(param));
        }
        this.params = typeParams;
    }

    private FunctionTypeSymbol(TypeSymbol returnType, List<TypeSymbol> params) {
        this.returnType = returnType;
        this.params = params;
    }


    public static FunctionTypeSymbol builder(TypeSymbol returnType, List<TypeSymbol> params) {
        return new FunctionTypeSymbol(returnType, params);
    }

    public void setReturnType(TypeSymbol returnType) {
        this.returnType = returnType;
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
