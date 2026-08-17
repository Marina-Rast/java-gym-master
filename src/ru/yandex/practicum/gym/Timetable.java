package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>(new TimeComparator()));
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> mapOfDay = timetable.get(day);

        if (!mapOfDay.containsKey(time)) {
            mapOfDay.put(time, new ArrayList<>());
        }
        mapOfDay.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();
        Map<TimeOfDay, List<TrainingSession>> mapOfDay = timetable.get(dayOfWeek);

        for (List<TrainingSession> session : mapOfDay.values()) {
            result.addAll(session);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> mapOfDay = timetable.get(dayOfWeek);
        return mapOfDay.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCount.put(coach, coachCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> list = new ArrayList<>(coachCount.entrySet());
        list.sort(new Comparator<Map.Entry<Coach, Integer>>() {
            @Override
            public int compare(Map.Entry<Coach, Integer> e1, Map.Entry<Coach, Integer> e2) {
                return e2.getValue() - e1.getValue();
            }
        });

        Map<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private static class TimeComparator implements Comparator<TimeOfDay> {
        @Override
        public int compare(TimeOfDay time1, TimeOfDay time2) {
            if (time1.getHours() != time2.getHours()) {
                return time1.getHours() - time2.getHours();
            }
            return time1.getMinutes() - time2.getMinutes();
        }
    }
}