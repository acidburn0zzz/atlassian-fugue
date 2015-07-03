package com.atlassian.fugue;

import com.google.common.base.Predicates;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.atlassian.fugue.Either.left;
import static com.atlassian.fugue.Either.right;
import static com.atlassian.fugue.EitherRightProjectionTest.reverseToEither;
import static com.atlassian.fugue.UtilityFunctions.addOne;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EitherRightBiasTest
{
    private final Either<String, Integer> l = left("heyaa!");
    private final Either<String, Integer> r = right(12);

    @Test
    public void mapRight()
    {
        assertThat(Either.<String, Integer>right(3).map(addOne), is(Either.<String, Integer>right(4)));
    }

    @Test
    public void mapLeft()
    {
        assertThat(Either.<String, Integer>left("foo").map(addOne), is(Either.<String, Integer>left("foo")));
    }

    @Test
    public void flatMapRight()
    {
        assertThat(Either.<Integer, String>right("!foo").flatMap(reverseToEither), is(Either.<Integer, String>right("oof!")));
    }

    @Test
    public void flatMapLeft()
    {
        assertThat(Either.<Integer, String>left(5).flatMap(reverseToEither), is(Either.<Integer, String>left(5)));
    }

    @Test
    public void leftMapRight()
    {
        assertThat(Either.<Integer, String>right("foo").leftMap(addOne), is(Either.<Integer, String>right("foo")));
    }

    @Test
    public void leftMapLeft()
    {
        assertThat(Either.<Integer, String>left(3).leftMap(addOne), is(Either.<Integer, String>left(4)));
    }

    @Test
    public void getOrElseSupplierRight()
    {
        assertThat(r.getOrElse(Suppliers.ofInstance(1)), is(12));
    }

    @Test
    public void getOrElseSupplierLeft()
    {
        assertThat(l.getOrElse(Suppliers.ofInstance(1)), is(1));
    }

    @Test
    public void getOrElseRight()
    {
        assertThat(r.getOrElse(1), is(12));
    }

    @Test
    public void getOrElseLeft()
    {
        assertThat(l.getOrElse(1), is(1));
    }

    @Test
    public void getOrNullRight()
    {
        assertThat(r.getOrNull(), is(12));
    }

    @Test
    public void getOrNullLeft()
    {
        assertNull(l.getOrNull());
    }

    @Test
    public void getOrErrorRight()
    {
        assertThat(r.getOrError(Suppliers.ofInstance("Error message")), is(12));
    }

    @Test (expected = AssertionError.class)
    public void getOrErrorLeft()
    {
        l.getOrError(Suppliers.ofInstance("Error message"));
    }

    @Test
    public void getOrErrorLeftMessage()
    {
        try
        {
            l.getOrError(Suppliers.ofInstance("Error message"));
        }
        catch (Error e)
        {
            assertThat(e.getMessage(), is("Error message"));
            return;
        }

        fail("No error thrown");
    }

    @Test
    public void getOrThrowRight()
    {
        assertThat(r.getOrThrow(Suppliers.ofInstance(new RuntimeException("Run Error"))), is(12));
    }

    @Test (expected = RuntimeException.class)
    public void getOrThrowLeft()
    {
        l.getOrThrow(Suppliers.ofInstance(new RuntimeException("Run Error")));
    }

    @Test
    public void getOrThrowLeftMessage()
    {
        try
        {
            l.getOrThrow(Suppliers.ofInstance(new RuntimeException("Run Error")));
        }
        catch (Throwable e)
        {
            assertThat(e.getMessage(), is("Run Error"));
            return;
        }

        fail("No error thrown");
    }

    @Test
    public void existsRight()
    {
        assertThat(r.exists(Predicates.equalTo(12)), is(true));
        assertThat(r.exists(Predicates.equalTo(11)), is(false));
    }

    @Test
    public void existsLeft()
    {
        assertThat(l.exists(Predicates.equalTo(12)), is(false));
    }

    @Test
    public void forallRight()
    {
        assertThat(r.forall(Predicates.equalTo(12)), is(true));
        assertThat(r.forall(Predicates.equalTo(11)), is(false));
    }

    @Test
    public void forallLeft()
    {
        assertThat(l.forall(Predicates.equalTo(12)), is(true));
    }

    @Test
    public void foreachRight()
    {
        final AtomicBoolean called = new AtomicBoolean(false);
        final Effect<Integer> effect = new Effect<Integer>()
        {
            @Override
            public void apply(final Integer integer)
            {
                called.set(true);
            }
        };

        r.foreach(effect);

        assertThat(called.get(), is(true));
    }

    @Test
    public void foreachLeft()
    {
        final AtomicBoolean called = new AtomicBoolean(false);
        final Effect<Integer> effect = new Effect<Integer>()
        {
            @Override
            public void apply(final Integer integer)
            {
                called.set(true);
            }
        };

        l.foreach(effect);

        assertThat(called.get(), is(false));
    }

    @Test
    public void filterRight()
    {
        assertThat(r.filter(Predicates.equalTo(12)), is(Option.some(r)));
        assertThat(r.filter(Predicates.equalTo(11)), Matchers.is(Option.<Either<String, Integer>>none()));
    }

    @Test
    public void filterLeft()
    {
        assertThat(l.filter(Predicates.equalTo(12)), Matchers.is(Option.<Either<String, Integer>>none()));
    }

}
