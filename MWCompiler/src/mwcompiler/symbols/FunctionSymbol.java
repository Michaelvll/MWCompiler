package mwcompiler.symbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mwcompiler.symbols.BaseTypeSymbol.*;

// Each function has exactly one FunctionSymbol
public class FunctionSymbol extends Symbol {
    private String funcName;
    private TypeSymbol returnType;
    private List<TypeSymbol> params;
    private SymbolTable classSymbolTable;



    public FunctionSymbol(TypeSymbol returnType, Instance instance, List<TypeSymbol> params) {
        this.returnType = returnType;
        this.funcName = instance.getName();
        this.params = params;
    }

    public FunctionSymbol(TypeSymbol returnType, String funcName, TypeSymbol... params) {
        this.returnType = returnType;
        this.funcName = funcName;
        this.params = new ArrayList<>(Arrays.asList(params));
    }

    private FunctionSymbol(TypeSymbol returnType, Instance instance, TypeSymbol... params) {
        this.returnType = returnType;
        this.funcName = instance.getName();
        this.params = new ArrayList<>(Arrays.asList(params));
    }

    private FunctionSymbol(TypeSymbol returnType, Instance instance) {
        this.returnType = returnType;
        this.funcName = instance.getName();
        this.params = new ArrayList<>();
    }


//    public static FunctionSymbol tmpBuilder(TypeSymbol returnType, List<TypeSymbol> params) {
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
    public SymbolInfo findIn(Instance instance) {
        throw new RuntimeException("(Type Checking) Function does not have a member ");
    }


    public void addNamePrefix(String prefix) {
        this.funcName = prefix + funcName;
    }

    public static FunctionSymbol MAIN;


    public static final FunctionSymbol PRINTF = new FunctionSymbol(VOID_TYPE_SYMBOL, Instance.PRINTF, STRING_TYPE_SYMBOL, INT_TYPE_SYMBOL);
    public static final FunctionSymbol PRINT = new FunctionSymbol(VOID_TYPE_SYMBOL, Instance.PRINT, STRING_TYPE_SYMBOL);
    public static final FunctionSymbol PRINTLN = new FunctionSymbol(VOID_TYPE_SYMBOL, Instance.PRINTLN, STRING_TYPE_SYMBOL);
    public static final FunctionSymbol GET_STRING = new FunctionSymbol(STRING_TYPE_SYMBOL, Instance.GET_STRING);
    // This will be solved in code gen
    public static final FunctionSymbol GET_INT = new FunctionSymbol(INT_TYPE_SYMBOL, Instance.GET_INT);
    public static final FunctionSymbol TO_STRING = new FunctionSymbol(STRING_TYPE_SYMBOL, Instance.TO_STRING, INT_TYPE_SYMBOL);
    public static final FunctionSymbol SIZE = new FunctionSymbol(INT_TYPE_SYMBOL, Instance.SIZE);
    public static final FunctionSymbol LENGTH = new FunctionSymbol(INT_TYPE_SYMBOL, Instance.LENGTH);
    public static final FunctionSymbol SUBSTRING = new FunctionSymbol(STRING_TYPE_SYMBOL, Instance.SUBSTRING, INT_TYPE_SYMBOL, INT_TYPE_SYMBOL);
    public static final FunctionSymbol PARSE_INT = new FunctionSymbol(INT_TYPE_SYMBOL, Instance.PARSE_INT);
    public static final FunctionSymbol ORD = new FunctionSymbol(INT_TYPE_SYMBOL, Instance.ORD, INT_TYPE_SYMBOL);


    public SymbolTable getClassSymbolTable() {
        return classSymbolTable;
    }

    public void setClassSymbolTable(SymbolTable classSymbolTable) {
        this.classSymbolTable = classSymbolTable;
    }
}
