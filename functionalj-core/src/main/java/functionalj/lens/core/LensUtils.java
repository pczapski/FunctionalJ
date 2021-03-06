package functionalj.lens.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import functionalj.lens.lenses.AnyLens;
import functionalj.lens.lenses.FuncListLens;
import functionalj.lens.lenses.ListLens;
import functionalj.lens.lenses.MapLens;
import functionalj.lens.lenses.NullableLens;
import functionalj.lens.lenses.ObjectLens;
import functionalj.lens.lenses.OptionalLens;
import functionalj.lens.lenses.ResultLens;
import functionalj.list.FuncList;
import functionalj.map.FuncMap;
import functionalj.result.Result;
import lombok.val;
import nawaman.nullablej.nullable.Nullable;

@SuppressWarnings("javadoc")
public class LensUtils {

    public static <HOST, DATA, SUB, SUBLENS> SUBLENS createSubLens(
            ObjectLens<HOST, DATA>                 dataLens,
            Function<DATA, SUB>                    readSub,
            WriteLens<DATA, SUB>                   writeSub,
            Function<LensSpec<HOST, SUB>, SUBLENS> subLensCreator) {
        val lensSpec    = dataLens.lensSpec();
        val hostSubSpec = lensSpec.then(LensSpec.of(readSub, writeSub, lensSpec.isNullSafe()));
        return subLensCreator.apply(hostSubSpec);
    }
    
    public static <DATA, SUB, HOST> Function<HOST, SUB> createSubRead(
            Function<HOST, DATA> readValue,
            Function<DATA, SUB>  readSub, 
            BooleanSupplier      isNullSafe) {
        return host ->{
            val value = readValue.apply(host);
            if (isNullSafe.getAsBoolean() && (value == null))
                return null;
            
            val subValue = readSub.apply(value);
            return subValue;
        };
    }
    
    public static <HOST, DATA, SUB> WriteLens<HOST, SUB> createSubWrite(
            Function<HOST, DATA>         readValue,
            WriteLens<HOST, DATA>        writeValue,
            BiFunction<DATA, SUB, DATA>  writeSub,
            BooleanSupplier              isNullSafe) {
        return createSubWrite(readValue, writeValue, WriteLens.of(writeSub), isNullSafe);
    }
    
    public static <HOST, DATA, SUB> WriteLens<HOST, SUB> createSubWrite(
            Function<HOST, DATA>  readValue,
            WriteLens<HOST, DATA> writeValue,
            WriteLens<DATA, SUB>  writeSub,
            BooleanSupplier       isNullSafe) {
        return (host, newSubValue)->{
            return performWrite(readValue, writeValue, writeSub, isNullSafe, host, newSubValue);
        };
    }
    
    private static <DATA, HOST, SUB> HOST performWrite(Function<HOST, DATA> readValue, WriteLens<HOST, DATA> writeValue,
            WriteLens<DATA, SUB> writeSub, BooleanSupplier isNullSafe, HOST host, SUB newSubValue) {
        val oldValue = readValue.apply(host);
        if (isNullSafe.getAsBoolean() && (oldValue == null))
            return host;
        
        val newValue = writeSub.apply(oldValue, newSubValue);
        val newHost  = writeValue.apply(host, newValue);
        return newHost;
    }
    
    //== Parameterized ==
    
    public static <HOST, TYPE, SUB, SUBLENS extends AnyLens<HOST, SUB>> 
        LensSpecParameterized<HOST, TYPE, SUB, SUBLENS> createLensSpecParameterized(
            Function<HOST, TYPE>                   read,
            WriteLens<HOST, TYPE>                  write,
            Function<LensSpec<HOST, SUB>, SUBLENS> subCreator) {
        val spec = new LensSpecParameterized<HOST, TYPE, SUB, SUBLENS>() {
            @Override
            public LensSpec<HOST, TYPE> getSpec() {
                return LensSpec.of(read, write);
            }
            @Override
            public SUBLENS createSubLens(LensSpec<HOST, SUB> subSpec) {
                return subCreator.apply(subSpec);
            }
        };
        return spec;
    }
    
    //== Nullable ==
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> NullableLens<HOST, TYPE, SUBLENS> 
        createNullableLens(
            Function<HOST, Nullable<TYPE>>          read,
            WriteLens<HOST, Nullable<TYPE>>         write,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val spec = createLensSpecParameterized(read, write, subCreator);
        val lens = (NullableLens<HOST, TYPE, SUBLENS>)()->spec;
        return lens;
    }
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> NullableLens<HOST, TYPE, SUBLENS> 
        createNullableLens(
            LensSpec<HOST, Nullable<TYPE>> nullableLensSpec,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val lens = createNullableLens(nullableLensSpec.getRead(), nullableLensSpec.getWrite(), subCreator);
        return lens;
    }
    
    //== Result ==
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> ResultLens<HOST, TYPE, SUBLENS> 
        createResultLens(
            Function<HOST, Result<TYPE>>          read,
            WriteLens<HOST, Result<TYPE>>         write,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val spec = createLensSpecParameterized(read, write, subCreator);
        val lens = (ResultLens<HOST, TYPE, SUBLENS>)()->spec;
        return lens;
    }
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> ResultLens<HOST, TYPE, SUBLENS> 
        createResultLens(
            LensSpec<HOST, Result<TYPE>> resultLensSpec,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val lens = createResultLens(resultLensSpec.getRead(), resultLensSpec.getWrite(), subCreator);
        return lens;
    }
    
    //== Optional ==
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> OptionalLens<HOST, TYPE, SUBLENS> 
        createOptionalLens(
            Function<HOST, Optional<TYPE>>          read,
            WriteLens<HOST, Optional<TYPE>>         write,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val spec = createLensSpecParameterized(read, write, subCreator);
        val lens = (OptionalLens<HOST, TYPE, SUBLENS>)()->spec;
        return lens;
    }
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> OptionalLens<HOST, TYPE, SUBLENS> 
        createOptionalLens(
            LensSpec<HOST, Optional<TYPE>> spec,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val lens = createOptionalLens(spec.getRead(), spec.getWrite(), subCreator);
        return lens;
    }
    
    //== List ==
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> ListLens<HOST, TYPE, SUBLENS> 
        createListLens(
            Function<HOST, List<TYPE>>              read,
            WriteLens<HOST, List<TYPE>>             write,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val spec = createLensSpecParameterized(read, write, subCreator);
        val listLens = ListLens.of(spec);
        return listLens;
    }
    
    public static <HOST, TYPE, TYPELENS extends AnyLens<HOST, TYPE>> ListLens<HOST, TYPE, TYPELENS>
            createSubListLens(
                LensSpec<HOST, List<TYPE>>                              spec,
                LensSpecParameterized<HOST, List<TYPE>, TYPE, TYPELENS> specParameterized,
                Function<HOST, List<TYPE>>                              read) {
        val newSpec = new LensSpecParameterized<HOST, List<TYPE>, TYPE, TYPELENS>() {
            @Override
            public LensSpec<HOST, List<TYPE>> getSpec() {
                return new LensSpec<>(read, spec.getWrite(), spec.getIsNullSafe());
            }
            @Override
            public TYPELENS createSubLens(LensSpec<HOST, TYPE> subSpec) {
                return specParameterized.createSubAccessFromHost(subSpec.getRead());
            }
        };
        return () -> newSpec;
    }
    
    //== Map ==
    
    public static <HOST, KEY, VALUE, KEYLENS extends AnyLens<HOST,KEY>, VALUELENS extends AnyLens<HOST,VALUE>>
            MapLens<HOST, KEY, VALUE, KEYLENS, VALUELENS> of(
                    Function<HOST,  Map<KEY, VALUE>>           read,
                    WriteLens<HOST, Map<KEY, VALUE>>           write,
                    Function<LensSpec<HOST, KEY>,   KEYLENS>   keyLensCreator,
                    Function<LensSpec<HOST, VALUE>, VALUELENS> valueLensCreator) {
        return MapLens.of(read, write, keyLensCreator, valueLensCreator);
    }
    
    public static <KEYLENS extends AnyLens<HOST, KEY>, HOST, VALUELENS extends AnyLens<HOST, VALUE>, KEY, VALUE>
            LensSpecParameterized2<HOST, Map<KEY, VALUE>, KEY, VALUE, KEYLENS, VALUELENS> createMapLensSpec(
                    Function<HOST,  Map<KEY, VALUE>>           read,
                    WriteLens<HOST, Map<KEY, VALUE>>           write,
                    Function<LensSpec<HOST, KEY>,   KEYLENS>   keyLensCreator,
                    Function<LensSpec<HOST, VALUE>, VALUELENS> valueLensCreator) {
        return new LensSpecParameterized2<HOST, Map<KEY, VALUE>, KEY, VALUE, KEYLENS, VALUELENS>() {

            @Override
            public LensSpec<HOST, Map<KEY, VALUE>> getSpec() {
                return LensSpec.of(read, write);
            }

            @Override
            public KEYLENS createSubLens1(
                    LensSpec<HOST, KEY> subSpec) {
                return keyLensCreator.apply(subSpec);
            }

            @Override
            public VALUELENS createSubLens2(
                    LensSpec<HOST, VALUE> subSpec) {
                return valueLensCreator.apply(subSpec);
            }
        };
    }
    
    //== FuncList ==
    
    public static <HOST, TYPE, SUBLENS extends AnyLens<HOST, TYPE>> FuncListLens<HOST, TYPE, SUBLENS> 
        createFuncListLens(
            Function<HOST,  FuncList<TYPE>>   read,
            WriteLens<HOST, FuncList<TYPE>>   write,
            Function<LensSpec<HOST, TYPE>, SUBLENS> subCreator) {
        val spec = createLensSpecParameterized(read, write, subCreator);
        val listLens = FuncListLens.of(spec);
        return listLens;
    }
    
    public static <HOST, TYPE, TYPELENS extends AnyLens<HOST, TYPE>> FuncListLens<HOST, TYPE, TYPELENS>
            createSubFuncListLens(
                LensSpec<HOST, FuncList<TYPE>>                              spec,
                LensSpecParameterized<HOST, FuncList<TYPE>, TYPE, TYPELENS> specParameterized,
                Function<HOST, FuncList<TYPE>>                              read) {
        val newSpec = new LensSpecParameterized<HOST, FuncList<TYPE>, TYPE, TYPELENS>() {
            @Override
            public LensSpec<HOST, FuncList<TYPE>> getSpec() {
                return new LensSpec<>(read, spec.getWrite(), spec.getIsNullSafe());
            }
            @Override
            public TYPELENS createSubLens(LensSpec<HOST, TYPE> subSpec) {
                return specParameterized.createSubLens(subSpec);
            }
        };
        return () -> newSpec;
    }
    
    // == FuncMap ==
    
    public static <KEYLENS extends AnyLens<HOST, KEY>, HOST, VALUELENS extends AnyLens<HOST, VALUE>, KEY, VALUE>
            LensSpecParameterized2<HOST, FuncMap<KEY, VALUE>, KEY, VALUE, KEYLENS, VALUELENS> createFuncMapLensSpec(
                    Function<HOST,  FuncMap<KEY, VALUE>>       read,
                    WriteLens<HOST, FuncMap<KEY, VALUE>>       write,
                    Function<LensSpec<HOST, KEY>,   KEYLENS>   keyLensCreator,
                    Function<LensSpec<HOST, VALUE>, VALUELENS> valueLensCreator) {
        return new LensSpecParameterized2<HOST, FuncMap<KEY, VALUE>, KEY, VALUE, KEYLENS, VALUELENS>() {
            
            @Override
            public LensSpec<HOST, FuncMap<KEY, VALUE>> getSpec() {
                return LensSpec.of(read, write);
            }
            
            @Override
            public KEYLENS createSubLens1(
                    LensSpec<HOST, KEY> subSpec) {
                return keyLensCreator.apply(subSpec);
            }
            
            @Override
            public VALUELENS createSubLens2(
                    LensSpec<HOST, VALUE> subSpec) {
                return valueLensCreator.apply(subSpec);
            }
        };
    }
    
}
