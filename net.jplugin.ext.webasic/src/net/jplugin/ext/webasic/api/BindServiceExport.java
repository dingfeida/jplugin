package net.jplugin.ext.webasic.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BindServiceExport {
	public String path();
}