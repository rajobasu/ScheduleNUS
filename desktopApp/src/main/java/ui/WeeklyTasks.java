package ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import me.rajobasu.shared.core.algo.Date;
import me.rajobasu.shared.core.algo.TaskChunk;

import java.time.LocalDate;
import java.util.Iterator;

/**
 * A weekly task list filtered from the given taskList.
 * It is used for the updating of UI through observables.
 * Supports a minimal set of list operations.
 */
public class WeeklyTasks implements Iterable<TaskChunk> {
    private static final Integer ACADEMIC_WEEK_OFFSET = 0;
    private static final Integer INITIAL_WEEK = 1;
    private static final Integer YEAR = 2021;
    private static final Integer YEAR_OFFSET = 3; // The first full week of 2021 begins on the 4th
    private static final Integer DAYS_IN_WEEK = 7;


    private final SimpleIntegerProperty weekNumber;
    private final SimpleIntegerProperty totalWeeklyTasks;
    private final SimpleIntegerProperty totalDoneTasks;
    private final SimpleDoubleProperty weeklyProgress;
    private final ObservableList<TaskChunk> allTaskList;
    private final ObservableList<FilteredList<TaskChunk>> weeklyTaskList;

    /**
     * Constructs a {@code WeeklyTasks}.
     * For the given week of the YEAR.
     *
     * @param taskList A valid task list.
     * @param date     A valid week of the year.
     */
    public WeeklyTasks(ObservableList<TaskChunk> taskList) {
        weeklyTaskList = FXCollections.observableArrayList();
        allTaskList = taskList;
        weekNumber = new SimpleIntegerProperty(1);
        totalWeeklyTasks = new SimpleIntegerProperty();
        totalDoneTasks = new SimpleIntegerProperty();
        weeklyProgress = new SimpleDoubleProperty();
        // Populate the weeklyTaskList with 7 dailyTaskList
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            weeklyTaskList.add(new FilteredList<>(allTaskList));
        }
        
    }

    /**
     * Returns a list of daily task list for the week
     */
    public ObservableList<FilteredList<TaskChunk>> getWeeklyList() {
        return weeklyTaskList;
    }

    /**
     * Returns a task list filtered by the day of week
     */
    public ObservableList<TaskChunk> getDailyTaskList(Integer dayOfWeek) {
        return weeklyTaskList.get(dayOfWeek);
    }

    /**
     * Returns the local date of the first day of the week
     */
    public LocalDate getFirstDayOfWeek() {
        LocalDate firstDay = LocalDate.ofYearDay(
                YEAR, (getWeekNumber() + ACADEMIC_WEEK_OFFSET) * DAYS_IN_WEEK - YEAR_OFFSET);
        return firstDay;
    }

    /**
     * Returns an integer value of the week number
     */
    public Integer getWeekNumber() {
        return weekNumber.getValue();
    }

    /**
     * Returns an observable value of the week number
     */
    public SimpleIntegerProperty getObservableWeekNumber() {
        return weekNumber;
    }

    /**
     * Sets the week number of the year
     */
    public void setWeek(Date date) {
        updateWeeklyList(date);
    }

    /**
     * Updates the weekly list to the week set
     */
    public void updateWeeklyList(Date date) {
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            Date newDate = date.add(i);
            weeklyTaskList.get(i).setPredicate(taskChunk -> taskChunk.getTimeChunk().getDate().equals(newDate));
        }
    }


    /**
     * Return the total tasks for the week
     */
    public SimpleIntegerProperty getTotalWeeklyTasks() {
        return totalWeeklyTasks;
    }

    /**
     * Return the total completed tasks for the week
     */
    public SimpleIntegerProperty getTotalDoneTasks() {
        return totalDoneTasks;
    }

    /**
     * Returns the weekly progress
     */
    public SimpleDoubleProperty getWeeklyProgress() {
        return weeklyProgress;
    }

    @Override
    public Iterator<TaskChunk> iterator() {
        return allTaskList.iterator();
    }
}
