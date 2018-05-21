package mwcompiler.symbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mwcompiler.symbols.NonArrayTypeSymbol.*;

// Each function has exactly one FunctionSymbol
public class FunctionSymbol extends TypeSymbol {
    private String funcName;
    private TypeSymbol returnType;
    private List<TypeSymbol> params;

    public FunctionSymbol(TypeSymbol returnType, InstanceSymbol instanceSymbol, List<TypeSymbol> params) {
        this.returnType = returnType;
        this.funcName = instanceSymbol.getName();
        this.params = params;
    }

    public FunctionSymbol(TypeSymbol returnType, String funcName, TypeSymbol... params) {
        this.returnType = returnType;
        this.funcName = funcName;
        this.params = new ArrayList<>(Arrays.asList(params));
    }

    private FunctionSymbol(TypeSymbol returnType, InstanceSymbol instanceSymbol, TypeSymbol... params) {
        this.returnType = returnType;
        this.funcName = instanceSymbol.getName();
        this.params = new ArrayList<>(Arrays.asList(params));
    }

    private FunctionSymbol(TypeSymbol returnType, InstanceSymbol instanceSymbol) {
        this.returnType = returnType;
        this.funcName = instanceSymbol.getName();
        this.params = new ArrayList<>();
    }


//    public static FunctionSymbol builder(TypeSymbol returnType, List<TypeSymbol> params) {
//        return new FunctionSymbol(returnType, params);
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
        return funcName;
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


    public void addNamePrefix(String prefix) {
        this.funcName = prefix + funcName;
    }

    public static final FunctionSymbol PRINT = new FunctionSymbol(VOID_TYPE_SYMBOL, InstanceSymbol.PRINT, STRING_TYPE_SYMBOL);
    public static final FunctionSymbol PRINTLN = new FunctionSymbol(VOID_TYPE_SYMBOL, InstanceSymbol.PRINTLN, STRING_TYPE_SYMBOL);
    public static final FunctionSymbol GET_STRING = new FunctionSymbol(STRING_TYPE_SYMBOL, InstanceSymbol.GET_STRING);
    public static final FunctionSymbol GET_INT = new FunctionSymbol(INT_TYPE_SYMBOL, InstanceSymbol.GET_INT);
    public static final FunctionSymbol TO_STRING = new FunctionSymbol(STRING_TYPE_SYMBOL, InstanceSymbol.TO_STRING, INT_TYPE_SYMBOL);
    public static final FunctionSymbol SIZE = new FunctionSymbol(INT_TYPE_SYMBOL, InstanceSymbol.SIZE);
    public static final FunctionSymbol LENGTH = new FunctionSymbol(INT_TYPE_SYMBOL, InstanceSymbol.LENGTH);
    public static final FunctionSymbol SUBSTRING = new FunctionSymbol(STRING_TYPE_SYMBOL, InstanceSymbol.SUBSTRING, INT_TYPE_SYMBOL, INT_TYPE_SYMBOL);
    public static final FunctionSymbol PARSE_INT = new FunctionSymbol(INT_TYPE_SYMBOL, InstanceSymbol.PARSE_INT);
    public static final FunctionSymbol ORD = new FunctionSymbol(INT_TYPE_SYMBOL, InstanceSymbol.ORD, INT_TYPE_SYMBOL);

}
