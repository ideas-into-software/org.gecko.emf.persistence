/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * This test was implemented to test the fix of issue #17 
 * (https://github.com/geckoprojects-org/org.gecko.emf.persistence/issues/17)
 * @author ilenia
 * @since Aug 17, 2023
 */
@Testable
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class QueryBuilderIntegrationTest {
	
	@Test
	public void testInQueryList() {
		SimpleQueryBuilder queryBuilder = new SimpleQueryBuilder();
		List<String> terms = List.of("test1", "test2", "test3");
		queryBuilder.in(terms);
		List<Object> inValueList = queryBuilder.getInValueList();
		assertThat(inValueList).isNotEmpty();
		Object obj = inValueList.get(0);
		assertThat(obj).isInstanceOf(String.class);
	}
	
	@Test
	public void testInQueryCollection() {
		SimpleQueryBuilder queryBuilder = new SimpleQueryBuilder();
		Collection<String> terms = Set.of("test1", "test2", "test3");
		queryBuilder.in(terms);
		List<Object> inValueList = queryBuilder.getInValueList();
		assertThat(inValueList).isNotEmpty();
		Object obj = inValueList.get(0);
		assertThat(obj).isInstanceOf(String.class);
	}
	
	@Test
	public void testInQueryVarargs() {
		SimpleQueryBuilder queryBuilder = new SimpleQueryBuilder();
		queryBuilder.in("test1", "test2", "test3");
		List<Object> inValueList = queryBuilder.getInValueList();
		assertThat(inValueList).isNotEmpty();
		Object obj = inValueList.get(0);
		assertThat(obj).isInstanceOf(String.class);
	}
}
