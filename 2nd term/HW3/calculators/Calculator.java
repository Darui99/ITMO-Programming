package calculators;

import expression.exceptions.EvaluateException;

public interface Calculator<T> {
    T add(T arg1, T arg2) throws EvaluateException;

    T sub(T arg1, T arg2) throws EvaluateException;

    T mul(T arg1, T arg2) throws EvaluateException;

    T div(T arg1, T arg2) throws EvaluateException;

    T neg(T arg) throws EvaluateException;

    T abs(T arg) throws EvaluateException;

    T sqr(T arg) throws EvaluateException;

    T mod(T arg1, T arg2) throws EvaluateException;

    T convertString(String s) throws Exception;
}