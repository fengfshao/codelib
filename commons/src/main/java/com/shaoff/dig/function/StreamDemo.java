package com.shaoff.dig.function;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamDemo {

    static class Student {
        String name;
        int score;

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Mr.wang", 78),
                new Student("Mr.lee", 83),
                new Student("Mr.shao", 61),
                new Student("Mr.lyu", 91));

        List<String> above90Names = students.stream()
                .filter(t -> t.getScore() > 90)
                // peek 可用户于debug等操作，对流中的元素增加一个操作
                //.peek(System.out::println)
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println(above90Names);

        Student min = students.stream().reduce((a, b) -> a.getScore() > b.getScore() ? b : a).get();
        Student max = students.stream().max((a, b) -> Double.compare(a.getScore(), b.getScore())).get();
        long count = students.stream().collect(Collectors.counting());
        System.out.printf(String.format("min=%s,max=%s,count=%d\n", min, max, count));
        Function<Student, String> classifier = student -> {
            if (student.getScore() < 60) {
                return "Fail";
            } else if (student.getScore() < 70) {
                return "Normal";
            } else if (student.getScore() < 80) {
                return "Good";
            } else {
                return "Outstanding";
            }
        };

        Collector<Student, ?, String> collector =
                Collectors.collectingAndThen(Collectors.toList(),
                        a -> a.stream().map(Student::getName).collect(Collectors.joining(", ")));

        // 根据分区区间统计名单
        Map<String, String> nameList = students.stream().collect(Collectors.groupingBy(classifier, collector));
        System.out.println(nameList);
        System.out.println(students.parallelStream().reduce((a, b) -> a.getScore() > b.getScore() ? b : a).get());
    }

}
