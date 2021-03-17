module jdbccmd {
	exports jdbccmd;

	requires java.sql;
	requires java.base;
	requires info.picocli;
	requires transitive podam;
	requires transitive slf4j.api;
}