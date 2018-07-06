package com.zufangbao.earth;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"!com.zufangbao.earth.api.test.post.*"})
public class AllTestsOfEarth {

}
