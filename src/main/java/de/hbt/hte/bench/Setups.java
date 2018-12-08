package de.hbt.hte.bench;

import de.hbt.hte.TemplateEngine;
import de.hbt.hte.rt.SimpleEnvironment;
import freemarker.template.*;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class Setups {

    @Benchmark
    public void hte(Blackhole bh) {
        bh.consume(TemplateEngine.instantiateTemplate("${1+2+3+4+5}", new SimpleEnvironment()));
    }

    @Benchmark
    public void freeMarker(Blackhole bh) throws Exception {
        Configuration cfg = new Configuration(new Version(2, 3, 28));
        cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(new Version(2, 3, 28)).build());
        Template t = new Template("templateName", new StringReader("${1+2+3+4+5}"), cfg);
        bh.consume(t);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Setups.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
