package de.hbt.hte.bench;

import de.hbt.hte.TemplateEngine;
import de.hbt.hte.rt.CompiledTemplate;
import de.hbt.hte.rt.Environment;
import de.hbt.hte.rt.SimpleEnvironment;
import freemarker.template.*;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class Arith {

    private static final Environment env = new SimpleEnvironment();

    private CompiledTemplate ct;
    private Template t;
    private Writer nullWriter = new Writer() {
        public void write(char[] cbuf, int off, int len) throws IOException {
        }
        public void flush() throws IOException {
        }
        public void close() throws IOException {
        }
    };

    @Setup
    public void setup() throws Exception {
        ct = TemplateEngine.instantiateTemplate("${1+2+3+4+5*2+4+5+6+7}", env);
        Configuration cfg = new Configuration(new Version(2, 3, 28));
        cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(new Version(2, 3, 28)).build());
        t = new Template("templateName", new StringReader("${1+2+3+4+5*2+4+5+6+7}"), cfg);
    }

    @Benchmark
    public void hte(Blackhole bh) throws Exception {
        ct.write(nullWriter);
    }

    @Benchmark
    public void freeMarker(Blackhole bh) throws Exception {
        t.process(null, nullWriter);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Arith.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
