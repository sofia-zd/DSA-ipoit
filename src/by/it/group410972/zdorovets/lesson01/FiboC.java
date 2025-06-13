package by.it.group410972.zdorovets.lesson01;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        // Шаг 1: Найдём период Пизано
        int[] pisano = new int[m * 6]; // максимум длины периода — 6*m
        pisano[0] = 0;
        pisano[1] = 1;

        int periodLength = 0;
        for (int i = 2; i < pisano.length; i++) {
            pisano[i] = (pisano[i - 1] + pisano[i - 2]) % m;
            if (pisano[i - 1] == 0 && pisano[i] == 1) {
                periodLength = i - 1;
                break;
            }
        }

        // Шаг 2: Найдём n % periodLength
        int remainder = (int) (n % periodLength);

        // Шаг 3: Вернём остаток от соответствующего числа Фибоначчи
        return pisano[remainder];
    }
}
