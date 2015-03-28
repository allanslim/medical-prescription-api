package com.codewarrior.csc686.project.util;


import java.util.function.Consumer;
import java.util.function.Function;


public class Either<LEFT, RIGHT> {

    private final LEFT left;
    private final RIGHT right;

    private Either(LEFT left, RIGHT right) {
        this.left = left;
        this.right = right;
    }

    public static <LEFT, RIGHT> Either<LEFT, RIGHT> left(LEFT value) {

        return new Either(value, null);
    }

    public static <LEFT, RIGHT> Either<LEFT, RIGHT> right(RIGHT value) {

        return new Either(null, value);
    }

    public boolean isRight() { return right != null; }

    public boolean isLeft() { return left != null; }

    public LEFT left() { return left; }

    public RIGHT right() { return right; }

    //
    //   scala> val w = Left(9)
    //   w: scala.util.Left[Int,Nothing] = Left(9)
    //
    //   scala> w.left.map( x => x + 1)
    //   res3: Product with Serializable with scala.util.Either[Int,Nothing] = Left(10)
    public <T> Either<T, RIGHT> mapLeft(Function<? super LEFT, ? extends T> leftFunction) {

        if (isLeft()) {
            return new Either(leftFunction.apply(left), right);
        }

        return new Either(left, right);
    }

    //    scala> val t = Right(3)
    //    t: scala.util.Right[Nothing,Int] = Right(3)
    //
    //    scala> t.right.map( x => x + 5)
    //    res1: Product with Serializable with scala.util.Either[Nothing,Int] = Right(8)
    public <T> Either<LEFT, T> mapRight(Function<? super RIGHT, ? extends T> rightFunction) {

        if (isRight()) {
            return new Either(left, rightFunction.apply(right));
        }
        return new Either(left, right);
    }


    public <T> Either<LEFT, T> flatMapRight(Function<? super RIGHT, ? extends Either<LEFT,T>> rightFunction) {

        if (isRight()) {
            return rightFunction.apply(right);
        }
        return new Either(left, right);
    }

    public <T> Either<T, RIGHT> flatMapLeft(Function<? super LEFT, ?  extends Either<T,RIGHT>> leftFunction) {

        if (isLeft()) {
            return leftFunction.apply(left);
        }

        return new Either(left, right);
    }



    public <T> T map(Function<? super LEFT, ? extends T> leftFunction, Function<? super RIGHT, ? extends T> rightFunction) {

        if (isLeft()) {
            return leftFunction.apply(left);
        }
        return rightFunction.apply(right);
    }

    public void apply(Consumer<? super LEFT> leftFunction, Consumer<? super RIGHT> rightFunction) {

        applyRight(rightFunction);
        applyLeft(leftFunction);
    }


    public void applyRight( Consumer<? super RIGHT> rightFunction) {
        if (rightFunction != null && isRight()) {
            rightFunction.accept(right);
        }
    }

    public void applyLeft(Consumer<? super LEFT> leftFunction) {
        if (leftFunction != null && isLeft()) {
            leftFunction.accept(left);
        }
    }

}
