package functionalj.ref;

import static functionalj.list.FuncList.listOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

import functionalj.ref.OverridableRef;
import functionalj.ref.Ref;
import lombok.val;

public class RefTest {

	@Test
	public void testNull() {
		val ref = Ref.of(String.class).defaultToNull();
		assertNull(ref.value());
	}
	
	@Test
	public void testValue() {
		val ref1 = Ref.ofValue("Value");
		assertEquals("Value", ref1.value());
		
		val ref2 = Ref.ofValue(42);
		assertEquals(42, (int)ref2.value());
	}
	
	@Test
	public void testFrom() {
		val ref1 = Ref.of(String.class).defaultFrom(()->"Value");
		assertEquals("Value", ref1.value());
		
		val ref2 = Ref.of(Integer.class).defaultFrom(()->42);
		assertEquals(42, (int)ref2.value());
		
		val counter = new AtomicInteger();
		val ref3 = Ref.of(Integer.class).defaultFrom(counter::getAndIncrement);
		assertEquals(0, (int)ref3.value());
		assertEquals(1, (int)ref3.value());
		assertEquals(2, (int)ref3.value());
	}
	
	@Test
	public void testCurrentRef() {
		val ref1 = Ref.of(String.class).defaultFrom(()->"OrgValue");
		val ref2 = ref1.overridable();
		assertEquals("OrgValue", ref2.value());
		
		OverridableRef.runWith(
				listOf(ref2.butWith("NewValue")),
				()->{
					assertEquals("NewValue", ref2.value());
				});
		
		assertEquals("OrgValue", ref2.value());
	}
	
	public static class Answer {
		private final int number;
		public Answer() { this(42); }
		public Answer(int number) { this.number = number; }
		@Override
		public String toString() { return "Answer [number=" + number + "]"; }
	}
	
	@Test
	public void testRefTo() {
		val ref1 = Ref.to(Answer.class);
		val ref2 = ref1.overridable();
		assertEquals("Answer [number=42]", "" + ref1.value());
		assertEquals("Answer [number=42]", "" + ref2.value());
		
		OverridableRef.runWith(
				listOf(
					ref1.butWith(new Answer(123)),
					ref2.butWith(new Answer(123))
				),
				()->{
					assertEquals("Answer [number=42]", "" + ref1.value());
					assertEquals("Answer [number=123]", "" + ref2.value());
				});
		
		assertEquals("Answer [number=42]", "" + ref1.value());
		assertEquals("Answer [number=42]", "" + ref2.value());
	}
	
	@Test
	public void testRefFunction() {
		Ref<Supplier<String>> ref = Ref.ofValue(()->"Hello world!");
		assertEquals("Hello world!", ref.value().get());
	}
	
	@Test
	public void testBasicRetain() {
		val counter0 = new AtomicInteger();
		val counter1 = new AtomicInteger();
		val counter2 = new AtomicInteger();
		val ref0     = Ref.of(Integer.class).defaultFrom(counter0::getAndIncrement);
		val ref1     = Ref.of(Integer.class).defaultFrom(counter1::getAndIncrement).retained().forever();
		val ref2     = Ref.of(Integer.class).defaultFrom(counter2::getAndIncrement).retained().never();
		assertEquals(0, ref0.value().intValue());
		assertEquals(1, ref0.value().intValue());
		assertEquals(2, ref0.value().intValue());
		assertEquals(0, ref1.value().intValue());
		assertEquals(0, ref1.value().intValue());
		assertEquals(0, ref1.value().intValue());
		assertEquals(0, ref2.value().intValue());
		assertEquals(1, ref2.value().intValue());
		assertEquals(2, ref2.value().intValue());
	}

}