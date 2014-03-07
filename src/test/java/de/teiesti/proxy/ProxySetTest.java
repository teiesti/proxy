package de.teiesti.proxy;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ProxySetTest {

	private Set<Integer> subjects;
	private ProxySet<Integer, Integer> proxies;
	
	
	@Before
	public void setup() throws Exception {
		subjects = new HashSet<>();
		proxies = new ProxySet<>(subjects, new Mapper<Integer, Integer>() {
			@Override
			public Integer getProxy(Integer subject) {
				return subject;
			}

			@Override
			public Integer getSubject(Integer proxy) {
				return proxy;
			}

			@Override
			public Class<?> getProxyClass() {
				return Integer.class;
			}

			@Override
			public Class<?> getSubjectClass() {
				return Integer.class;
			}
		});
	}

	/*@Test
	public void testProxySet() {
		fail("Not yet implemented"); // TODO
	}*/

	@Test
	public void subjects_add_single() {
		subjects.add(42);

		assertThat(subjects.size(), is(1));
		assertThat(subjects, everyItem(is(new Integer(42))));

		assertThat(proxies.size(), is(1));
		assertThat(proxies, everyItem(is(new Integer(42))));
	}

	@Test
	public void proxies_add_single() {
		proxies.add(42);

		assertThat(subjects.size(), is(1));
		assertThat(subjects, everyItem(is(new Integer(42))));

		assertThat(proxies.size(), is(1));
		assertThat(proxies, everyItem(is(new Integer(42))));
	}

	@Test
	public void subjects_add_same() {
		proxies.add(42);
		proxies.add(42);

		assertThat(subjects.size(), is(1));
		assertThat(subjects, everyItem(is(new Integer(42))));

		assertThat(proxies.size(), is(1));
		assertThat(proxies, everyItem(is(new Integer(42))));
	}

	@Test
	public void proxies_add_same() {
		proxies.add(42);
		proxies.add(42);

		assertThat(subjects.size(), is(1));
		assertThat(subjects, everyItem(is(new Integer(42))));

		assertThat(proxies.size(), is(1));
		assertThat(proxies, everyItem(is(new Integer(42))));
	}

	@Test
	public void subjects_add_multipleWithSame() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });

		for (Integer n : toBeAdded)
			subjects.add(n);

		Set<Integer> expected = new HashSet<>(toBeAdded);

		assertThat(subjects, is(expected));
		assertThat(proxies, is(expected));
	}

	@Test
	public void proxies_add_multipleWithSame() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });

		for (Integer n : toBeAdded)
			proxies.add(n);

		Set<Integer> expected = new HashSet<>(toBeAdded);

		assertThat(subjects, is(expected));
		assertThat(proxies, is(expected));
	}

	@Test
	public void subjects_addAll() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });

		subjects.addAll(toBeAdded);

		Set<Integer> expected = new HashSet<>(toBeAdded);

		assertThat(subjects, is(expected));
		assertThat(proxies, is(expected));
	}

	@Test
	public void proxies_addAll() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });

		proxies.addAll(toBeAdded);

		Set<Integer> expected = new HashSet<>(toBeAdded);

		assertThat(subjects, is(expected));
		assertThat(proxies, is(expected));
	}

	@Test
	public void subjects_isEmptyClear() {
		assertTrue(subjects.isEmpty());
		assertTrue(proxies.isEmpty());
		
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		subjects.addAll(toBeAdded);
	
		assertTrue(!subjects.isEmpty());
		assertTrue(!proxies.isEmpty());
		
		subjects.clear();
		
		assertTrue(subjects.isEmpty());
		assertTrue(proxies.isEmpty());
	}
	
	@Test
	public void proxies_isEmptyClear() {
		assertTrue(subjects.isEmpty());
		assertTrue(proxies.isEmpty());
		
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		proxies.addAll(toBeAdded);
		
		assertTrue(!subjects.isEmpty());
		assertTrue(!proxies.isEmpty());
		
		proxies.clear();
		
		assertTrue(subjects.isEmpty());
		assertTrue(proxies.isEmpty());
	}

	@Test
	public void subjects_contains() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 1, 2, 3 });
		subjects.addAll(toBeAdded);

		for (Integer n : toBeAdded) {
			assertTrue(subjects.contains(n));
			assertTrue(proxies.contains(n));
		}
		
		for (Integer n : new Integer[] {0, 4, 42}) {
			assertFalse(subjects.contains(n));
			assertFalse(proxies.contains(n));
		}
		
		subjects.remove(2);
		
		assertFalse(subjects.contains(2));
		assertFalse(proxies.contains(2));
	}
	
	@Test
	public void proxies_contains() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 1, 2, 3 });
		proxies.addAll(toBeAdded);

		for (Integer n : toBeAdded) {
			assertTrue(subjects.contains(n));
			assertTrue(proxies.contains(n));
		}
		
		for (Integer n : new Integer[] {0, 4, 42}) {
			assertFalse(subjects.contains(n));
			assertFalse(proxies.contains(n));
		}
		
		proxies.remove(2);
		
		assertFalse(subjects.contains(2));
		assertFalse(proxies.contains(2));
	}

	@Test
	public void subjects_containsAll() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 1, 2, 3, 4 });
		subjects.addAll(toBeAdded);

		assertTrue(subjects.containsAll(toBeAdded));
		assertTrue(proxies.containsAll(toBeAdded));

		List<Integer> reordered = Arrays.asList(new Integer[] { 4, 1, 3, 2 });
		assertTrue(subjects.containsAll(reordered));
		assertTrue(proxies.containsAll(reordered));

		List<Integer> subset = Arrays.asList(new Integer[] { 4, 2 });
		assertTrue(subjects.containsAll(subset));
		assertTrue(proxies.containsAll(subset));

		List<Integer> superset = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 });
		assertFalse(subjects.containsAll(superset));
		assertFalse(proxies.containsAll(superset));

		List<Integer> empty = new ArrayList<>();
		assertTrue(subjects.containsAll(empty));
		assertTrue(proxies.containsAll(empty));

		List<Integer> weiredSubset = Arrays.asList(new Integer[] { 3, 1, 1, 4, 1, 3 });
		assertTrue(subjects.containsAll(weiredSubset));
		assertTrue(proxies.containsAll(weiredSubset));
	}

	@Test
	public void proxies_containsAll() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 1, 2, 3, 4 });
		proxies.addAll(toBeAdded);

		assertTrue(subjects.containsAll(toBeAdded));
		assertTrue(proxies.containsAll(toBeAdded));

		List<Integer> reordered = Arrays.asList(new Integer[] { 4, 1, 3, 2 });
		assertTrue(subjects.containsAll(reordered));
		assertTrue(proxies.containsAll(reordered));

		List<Integer> subset = Arrays.asList(new Integer[] { 4, 2 });
		assertTrue(subjects.containsAll(subset));
		assertTrue(proxies.containsAll(subset));

		List<Integer> superset = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 });
		assertFalse(subjects.containsAll(superset));
		assertFalse(proxies.containsAll(superset));

		List<Integer> empty = new ArrayList<>();
		assertTrue(subjects.containsAll(empty));
		assertTrue(proxies.containsAll(empty));

		List<Integer> weiredSubset = Arrays.asList(new Integer[] { 3, 1, 1, 4, 1, 3 });
		assertTrue(subjects.containsAll(weiredSubset));
		assertTrue(proxies.containsAll(weiredSubset));
	}

	@Test
	public void proxies_iterator() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		proxies.addAll(toBeAdded);
		
		Iterator<Integer> it = proxies.iterator();
		
		assertNotNull(it);
		
		while(it.hasNext()) {	
			assertTrue(toBeAdded.contains(it.next()));
			it.remove();
		}
		
		assertTrue(subjects.isEmpty());
		assertTrue(proxies.isEmpty());
	}

	@Test
	public void subjects_iterator() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		subjects.addAll(toBeAdded);
		
		Iterator<Integer> it = proxies.iterator();
		
		assertNotNull(it);
		
		while(it.hasNext()) {	
			assertTrue(toBeAdded.contains(it.next()));
			it.remove();
		}
		
		assertTrue(subjects.isEmpty());
		assertTrue(proxies.isEmpty());
	}
	
	/*
	@Test
	public void testRemove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRetainAll() {
		fail("Not yet implemented"); // TODO
	}*/

	@Test
	public void subjects_size() {
		assertThat(subjects.size(), is(0));
		assertThat(proxies.size(), is(0));
		
		for (int i = 1; i <= 10; i++) {
			subjects.add(i);
			assertThat(subjects.size(), is(i));
			assertThat(proxies.size(), is(i));
		}
		
		for (int i = 1; i <= 10; i++) {
			subjects.add(i);
			assertThat(subjects.size(), is(10));
			assertThat(proxies.size(), is(10));
		}
		
		for (int i = 1; i <= 10; i++) {
			subjects.remove(i);
			assertThat(subjects.size(), is(10 - i));
			assertThat(proxies.size(), is(10 - i));
		}
		
		assertThat(subjects.size(), is(0));
		assertThat(proxies.size(), is(0));
	}
	
	@Test
	public void proxies_size() {
		assertThat(subjects.size(), is(0));
		assertThat(proxies.size(), is(0));
		
		for (int i = 1; i <= 10; i++) {
			proxies.add(i);
			assertThat(subjects.size(), is(i));
			assertThat(proxies.size(), is(i));
		}
		
		for (int i = 1; i <= 10; i++) {
			proxies.add(i);
			assertThat(subjects.size(), is(10));
			assertThat(proxies.size(), is(10));
		}
		
		for (int i = 1; i <= 10; i++) {
			proxies.remove(i);
			assertThat(subjects.size(), is(10 - i));
			assertThat(proxies.size(), is(10 - i));
		}
		
		assertThat(subjects.size(), is(0));
		assertThat(proxies.size(), is(0));
	}

	@Test
	public void subject_toArray() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		subjects.addAll(toBeAdded);
		
		Object[] expected = new HashSet<>(toBeAdded).toArray();

		assertThat(subjects.toArray(), is(expected));
		assertThat(proxies.toArray(), is(expected));
	}
	
	@Test
	public void proxies_toArray() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		proxies.addAll(toBeAdded);
		
		Object[] expected = new HashSet<>(toBeAdded).toArray();

		assertThat(subjects.toArray(), is(expected));
		assertThat(proxies.toArray(), is(expected));
	}

	@Test
	public void subjects_toArrayTArray() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		subjects.addAll(toBeAdded);
		
		Set<Integer> expectedSet = new HashSet<>(toBeAdded);
		Integer[] expected = expectedSet.toArray(new Integer[0]);

		assertThat(subjects.toArray(new Integer[0]), is(expected));
		assertThat(proxies.toArray(new Integer[0]), is(expected));
		
		expected = expectedSet.toArray(new Integer[expectedSet.size()]);
		
		assertThat(subjects.toArray(new Integer[subjects.size()]), is(expected));
		assertThat(proxies.toArray(new Integer[proxies.size()]), is(expected));
	}
	
	@Test
	public void proxies_toArrayTArray() {
		List<Integer> toBeAdded = Arrays.asList(new Integer[] { 0, 0, 1, 2, 3, 1, 5, 5, 3, 1, 0, 7, 6 });
		proxies.addAll(toBeAdded);
		
		Set<Integer> expectedSet = new HashSet<>(toBeAdded);
		Integer[] expected = expectedSet.toArray(new Integer[0]);

		assertThat(subjects.toArray(new Integer[0]), is(expected));
		assertThat(proxies.toArray(new Integer[0]), is(expected));
		
		expected = expectedSet.toArray(new Integer[expectedSet.size()]);

		assertThat(subjects.toArray(new Integer[subjects.size()]), is(expected));
		assertThat(proxies.toArray(new Integer[proxies.size()]), is(expected));
	}

}
