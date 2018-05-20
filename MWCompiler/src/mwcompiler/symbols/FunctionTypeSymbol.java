package mwcompiler.symbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mwcompiler.symbols.NonArrayTypeSymbol.*;

public class FunctionTypeSymbol extends TypeSymbol {
    private TypeSymbol returnType;
    private List<TypeSymbol> params;

    public FunctionTypeSymbol(TypeSymbol returnType, List<TypeSymbol> params) {
        this.returnType = returnType;
        this.params = params;
    }

    private FunctionTypeSymbol(TypeSymbol returnType, TypeSymbol... params) {
        this.returnType = returnType;
        this.params = new ArrayList<>(Arrays.asList(params));
    }

    private FunctionTypeSymbol(TypeSymbol returnType) {
        this.returnType = returnType;
        this.params = new ArrayList<>();
    }


//    public static FunctionTypeSymbol builder(TypeSymbol returnType, List<TypeSymbol> params) {
//        return new FunctionTypeSymbol(returnType, params);
//    }

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
        params.forEach(param -> name.append("\t").append(param.getName()).append("\n"));
        return String.valueOf(name);
    }

    @Override
    public void checkLegal() {
        returnType.checkLegal();
        params.forEach(TypeSymbol::checkLegal);
    }

    @Override
    public SymbolInfo findIn(InstanceSymbol instanceSymbol) {
        throw new RuntimeException("(Type Checking) Function does not have a member ");
    }

    public static final FunctionTypeSymbol PRINT = new FunctionTypeSymbol(VOID_TYPE_SYMBOL, STRING_TYPE_SYMBOL);
    public static final FunctionTypeSymbol PRINTLN = new FunctionTypeSymbol(VOID_TYPE_SYMBOL, STRING_TYPE_SYMBOL);
    public static final FunctionTypeSymbol GET_STRING = new FunctionTypeSymbol(STRING_TYPE_SYMBOL);
    public static final FunctionTypeSymbol GET_INT = new FunctionTypeSymbol(INT_TYPE_SYMBOL);
    public static final FunctionTypeSymbol TO_STRING = new FunctionTypeSymbol(STRING_TYPE_SYMBOL, INT_TYPE_SYMBOL);
    public static final FunctionTypeSymbol SIZE = new FunctionTypeSymbol(INT_TYPE_SYMBOL);
    public static final FunctionTypeSymbol LENGTH = new FunctionTypeSymbol(INT_TYPE_SYMBOL);
    public static final FunctionTypeSymbol SUBSTRING = new FunctionTypeSymbol(STRING_TYPE_SYMBOL, INT_TYPE_SYMBOL, INT_TYPE_SYMBOL);
    public static final FunctionTypeSymbol PARSE_INT = new FunctionTypeSymbol(INT_TYPE_SYMBOL);
    public static final FunctionTypeSymbol ORD = new FunctionTypeSymbol(INT_TYPE_SYMBOL, INT_TYPE_SYMBOL);
}
