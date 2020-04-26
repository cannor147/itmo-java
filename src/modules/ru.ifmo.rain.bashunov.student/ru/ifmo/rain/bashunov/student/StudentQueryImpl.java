package ru.ifmo.rain.bashunov.student;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentGroupQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class StudentQueryImpl implements StudentGroupQuery {

    /* private fields */

    private static final Student DEFAULT_STUDENT = new Student(0, "", "", "");
    private static final Map.Entry<String, List<Student>> DEFAULT_PRE_GROUP = new AbstractMap.SimpleEntry<>("", Collections.singletonList(DEFAULT_STUDENT));
    private static final Comparator<Student> NAME_COMPARATOR = Comparator
            .comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .thenComparing(Student::compareTo);


    /* implementation of StudentGroupQuery */

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return mapAndCollect(students.stream(), Student::getFirstName, Collectors.toList());
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return mapAndCollect(students.stream(), Student::getLastName, Collectors.toList());
    }

    @Override
    public List<String> getGroups(List<Student> students) {
        return mapAndCollect(students.stream(), Student::getGroup, Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return mapAndCollect(students.stream(), this::getFullName, Collectors.toList());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMinStudentFirstName(List<Student> students) {
        return students.stream().min(Comparator.naturalOrder()).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return students.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return students.stream().sorted(NAME_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return filterAndSortByNameAndCollect(students.stream(), student -> student.getFirstName().equals(name), Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return filterAndSortByNameAndCollect(students.stream(), student -> student.getLastName().equals(name), Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, String name) {
        return filterAndSortByNameAndCollect(students.stream(), student -> student.getGroup().equals(name), Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
        return filterAndSortByNameAndCollect(students.stream(), student -> student.getGroup().equals(group),
                Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroupStream(students.stream(), NAME_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroupStream(students.stream(), Comparator.naturalOrder()).collect(Collectors.toList());
    }

    @Override
    public String getLargestGroup(Collection<Student> students) {
        return getPreGroupStream(students.stream()).max(Comparator.comparingInt(p -> p.getValue().size())).orElse(DEFAULT_PRE_GROUP).getKey();
    }

    @Override
    public String getLargestGroupFirstName(Collection<Student> students) {
        return getPreGroupStream(students.stream()).max(Comparator.comparingInt(p -> getDistinctFirstNames(p.getValue()).size())).orElse(DEFAULT_PRE_GROUP).getKey();
    }


    /* private methods */

    private String getFullName(Student student) {
        // this returns full name of the student
        return student.getFirstName() + " " + student.getLastName();
    }

    private Stream<Map.Entry<String, List<Student>>> getPreGroupStream(Stream<Student> studentStream) {
        // this creates a stream of groups, where group is a pair of String name and List<Student> students
        return studentStream.collect(Collectors.groupingBy(Student::getGroup, TreeMap::new, Collectors.toList())).entrySet().stream();
    }

    private Stream<Group> getGroupStream(Stream<Student> studentStream, Comparator<? super Student> comparator) {
        // this creates a stream of groups
        return getPreGroupStream(studentStream).map(e -> new Group(e.getKey(), e.getValue().stream().sorted(comparator).collect(Collectors.toList())));
    }

    private <T> T filterAndSortByNameAndCollect(Stream<Student> studentStream, Predicate<? super Student> predicate, Collector<? super Student, ?, T> collector) {
        // this collects the result of filtered sorted stream
        return studentStream.filter(predicate).sorted(StudentQueryImpl.NAME_COMPARATOR).collect(collector);
    }

    private <S, T extends Collection<S>> T mapAndCollect(Stream<Student> studentStream, Function<? super Student, ? extends S> mapper, Collector<? super S, ?, T> collector) {
        // this collects the result of mapping of stream
        return studentStream.map(mapper).collect(collector);
    }

}