package functionalj.lens.core;

import functionalj.lens.lenses.AnyAccess;
import functionalj.lens.lenses.AnyLens;

@SuppressWarnings("javadoc")
public abstract class AbstractLensType<H, T, TA extends AnyAccess<H, T>, TL extends AnyLens<H, T>> 
        implements LensType<H, T, TA, TL> {
    
    private final Class<T>  dataClass;
    private final Class<TA> accessClass;
    private final Class<TL> lensClass;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected AbstractLensType(Class<T> dataClass, Class<? extends AnyAccess> accessClass, Class<? extends AnyLens> lensClass) {
        this.dataClass   = dataClass;
        this.accessClass = (Class)accessClass;
        this.lensClass   = (Class)lensClass;
    }

    @Override
    public Class<T> getDataClass() {
        return dataClass;
    }

    @Override
    public Class<TA> getAccessClass() {
        return accessClass;
    }

    @Override
    public Class<TL> getLensClass() {
        return lensClass;
    }
    
}