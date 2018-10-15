package functionalj.types.ref;

import java.util.List;

import functionalj.functions.Func0;
import functionalj.types.result.Result;
import lombok.val;

public class OverridableRef<DATA> extends RefOf<DATA> {
	
	@SuppressWarnings("rawtypes")
	private static class Entry {
		
		private final Entry        parent;
		private final Substitution substitution;
		
		Entry(Entry parent, Substitution substitution) {
			this.parent       = parent;
			this.substitution = substitution;
		}
		
		@SuppressWarnings("unchecked")
		public <D> Func0<D> findSupplier(Ref<D> ref) {
			if (ref == null)
				return null;
			
			if (substitution != null) {
				if (ref.equals(substitution.ref())) {
					return substitution.supplier();
				}
			}
			
			if (parent == null)
				return null;
			
			return parent.findSupplier(ref);
		}
		
		@Override
		public String toString() {
			return "Entry [parent=" + parent + ", substitution=" + substitution + "]";
		}
		
	}
	
	private static final ThreadLocal<Entry> refEntry = ThreadLocal.withInitial(()->new Entry(null, null));
	
	private final Ref<DATA> defaultRef;
	
	public OverridableRef(Ref<DATA> defaultRef) {
		super(defaultRef.getDataType());
		this.defaultRef = defaultRef;
	}
	
	@Override
	public final Result<DATA> get() {
		val entry    = refEntry.get();
		val supplier = entry.findSupplier(this);
		if (supplier != null) {
			val result = Result.from(supplier);
			return result;
		}
		if (defaultRef != null) {
			val result = defaultRef.get();
			return result;
		}
		
		return Result.ofNotAvailable();
	}
	
	static final <V, E extends Exception> 
			V runWith(List<Substitution<?>> substitutions, ComputeBody<V, E> action) throws E {
		val map = refEntry.get();
		try {
			if (substitutions != null) {
				for (val substitution : substitutions) {
					if (substitution == null)
						continue;
					
					val newEntry = new Entry(map, substitution);
					refEntry.set(newEntry);
				}
			}
			
			return action.run();
		} finally {
			refEntry.set(map);
		}
	}
	
	static final <V, E extends Exception> 
			void runWith(List<Substitution<?>> substitutions, RunBody<E> action) throws E {
		val map = refEntry.get();
		try {
			if (substitutions != null) {
				for (val substitution : substitutions) {
					if (substitution == null)
						continue;
					
					val newEntry = new Entry(map, substitution);
					refEntry.set(newEntry);
				}
			}
			
			action.run();
		} finally {
			refEntry.set(map);
		}
	}
	
}