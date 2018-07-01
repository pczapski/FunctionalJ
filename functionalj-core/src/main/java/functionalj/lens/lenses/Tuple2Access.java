package functionalj.lens.lenses;

import java.util.function.Function;

import functionalj.lens.core.AccessParameterized2;
import functionalj.types.Tuple2;

public interface Tuple2Access<HOST, T1, T2, 
                        T1ACCESS extends AnyAccess<HOST,T1>, 
                        T2ACCESS extends AnyAccess<HOST,T2>>
        extends AccessParameterized2<HOST, Tuple2<T1, T2>, T1, T2, T1ACCESS, T2ACCESS> {

    public AccessParameterized2<HOST, Tuple2<T1, T2>, T1, T2, T1ACCESS, T2ACCESS> accessParameterized2();
    
    @Override
    public default Tuple2<T1, T2> apply(HOST host) {
        return accessParameterized2().apply(host);
    }

    @Override
    public default T1ACCESS createSubAccess1(Function<Tuple2<T1, T2>, T1> accessToParameter) {
        return accessParameterized2().createSubAccess1(Tuple2::_1);
    }

    @Override
    public default T2ACCESS createSubAccess2(Function<Tuple2<T1, T2>, T2> accessToParameter) {
        return accessParameterized2().createSubAccess2(Tuple2::_2);
    }

    @Override
    default T1ACCESS createSubAccessFromHost1(Function<HOST, T1> accessToParameter) {
        return accessParameterized2().createSubAccessFromHost1(accessToParameter);
    }

    @Override
    default T2ACCESS createSubAccessFromHost2(Function<HOST, T2> accessToParameter) {
        return accessParameterized2().createSubAccessFromHost2(accessToParameter);
    }
    
    public default T1ACCESS _1() {
        return accessParameterized2().createSubAccess1(Tuple2::_1);
    }
    public default T2ACCESS _2() {
        return accessParameterized2().createSubAccess2(Tuple2::_2);
    }
}
