package mwcompiler.symbols;

import mwcompiler.ast.nodes.TypeNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends InstanceSymbol {

    static {
        symbolMap.put("print", new FunctionSymbol("void", "print", "string"));
        symbolMap.put("println", new FunctionSymbol("void", "println", "string"));
        symbolMap.put("getString", new FunctionSymbol("string", "getString"));
        symbolMap.put("getInt", new FunctionSymbol("int", "getInt"));
        symbolMap.put("toString", new FunctionSymbol("string", "toString", "int"));
    }

    private String functionName;
    private TypeSymbol returnType;
    private List<TypeSymbol> params;

    private FunctionSymbol(String returnType, String functionName, String... params) {
        this.functionName = functionName;
        this.returnType = NonArrayTypeSymbol.getSymbol(returnType);
        List<TypeSymbol> typeParams = new ArrayList<>();
        for (String param : params) {
            typeParams.add(NonArrayTypeSymbol.getSymbol(param));
        }
        this.params = typeParams;
    }
    private FunctionSymbol(TypeNode returnType, String functionName, TypeNode... params) {
        this.functionName = functionName;
        this.returnType = returnType.getSymbol();
        List<TypeSymbol> typeParams = new ArrayList<>();
        for (TypeNode param : params) {
            typeParams.add(param.getSymbol());
        }
        this.params = typeParams;
    }


    private FunctionSymbol(String functionName) {
        this.functionName = functionName;
    }

    public static FunctionSymbol builder(String functionName) {
        String intern = functionName.intern();
        Symbol search = symbolMap.get(intern);
        if (search != null) {
            throw new RuntimeException("Redeclare a function");
        }
        search = new FunctionSymbol(functionName);
        symbolMap.put(intern, search);
        return (FunctionSymbol) search;
    }

    @Override
    public String getName() {
        return functionName;
    }

    public TypeSymbol getReturnType() {
        return returnType;
    }

    public void setFunctionTypes(TypeNode returnType, List<TypeNode> params) {
        this.returnType = returnType.getSymbol();


        List<TypeSymbol> typeParams = new ArrayList<>();
        if (this.returnType == null) {
            throw new RuntimeException("Unknown function return typename");
        }
        for (Integer index = 0; index < params.size(); ++index) {
            TypeSymbol typeParam = params.get(index).getSymbol();
            if (typeParam == null) {
                throw new RuntimeException("Unknown parameter(" + String.valueOf(index) + ") typename");
            }
            typeParams.add(typeParam);
        }
        this.params = typeParams;
    }

    public List<TypeSymbol> getParams() {
        return params;
    }


}
