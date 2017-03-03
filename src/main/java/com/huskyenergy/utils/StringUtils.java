package com.huskyenergy.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static boolean containsAny(final String str,
									  final String[] searchStrs) {
		
		return containsChooseCaseAtIndex(str, searchStrs, false) > -1;
	}

	private static int containsChooseCaseAtIndex(final String str,
												 final String[] searchStrs,
												 final boolean ignoreCase) {
		
		if (str == null || (searchStrs == null || searchStrs.length < 1)) {
			return -1;
		}

		for (int i = 0; i < searchStrs.length; i++) {
			final String strToSearch = searchStrs[i];
			final boolean contains = (ignoreCase) ? 
							containsIgnoreCase(str, strToSearch) :
							contains(str, strToSearch);
			
			if (contains) {
				return i;
			}
		}
		return -1;
	}

}
