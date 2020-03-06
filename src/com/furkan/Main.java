package com.furkan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static LinkedList<String> mainLinkedList = new LinkedList<>();
    /*
    private static String[] list1 = {"pouring gas", "laughing", "lighting match", "fire"};
    private static String[] list2 = {"buying gas", "pouring gas", "crying", "fire", "smoke"};
    //*/

    // exampledata.json
    /*
    private static String[] list1 = {"fight", "gunshot", "fleeing"};
    private static String[] list2 = {"gunshot", "falling", "fleeing"};
    //*/
    // exampledata2.json
    /*
    private static String[] list1 = {"shouting", "fight", "fleeing"};
    private static String[] list2 = {"fight", "gunshot", "panic", "fleeing"};
    private static String[] list3 = {"anger", "shouting"};
    //*/
    // exampledata3.json
    /*
    private static String[] list1 = {"argument", "stuff", "pointing"};
    private static String[] list2 = {"press brief", "scandal", "pointing"};
    private static String[] list3 = {"bribe", "coverup"};
    //*/
    // exampledata4.json
    /*
    private static String[] list1 = {"shadowy figure", "demands", "scream", "siren"};
    private static String[] list2 = {"shadowy figure", "pointed gun", "scream"};
    //*/
    //*
    private static String[] list1 = {"argument", "stuff", "pointing"};
    private static String[] list2 = {"press brief", "stuff", "scandal", "pointing"};
    private static String[] list3 = {"bribe", "coverup", "stuff"};
    private static String[] list4 = {"bribe", "pointing"};
    //*/

    private static String lastAddedString = null;

    public static void main(String[] args) {
        List<String[]> listOfArrays = addArraysToList(list1, list2, list3, list4);
        List<LinkedList<String>> witnesses = addListOfArraysToListOfLinkedLists(listOfArrays);
        List<List<String>> scenarios = new ArrayList<List<String>>();
        boolean isScenarioFinished = false;
        int currentWitnessIndex = 0;
        while (witnesses.size() > currentWitnessIndex) {
            // no need to duplicate scenarios, just get out of the loop
            if (!isAnyAccountLeft(witnesses, scenarios)) {
                break;
            }
            List<String> currentScenario = new ArrayList<String>();
            for (int indexWitnesses = 0; indexWitnesses < witnesses.size(); indexWitnesses++) {
                // only look for current focused witness and latter ones to avoid duplicates
                if (indexWitnesses < currentWitnessIndex) {
                    continue;
                }
                LinkedList<String> currentWitness = witnesses.get(indexWitnesses);
                for (int i = 0; i < currentWitness.size(); i++) {
                    String account = currentWitness.get(i);
                    currentScenario = searchLeadingAccountInOtherWitnesses(witnesses, indexWitnesses, account, currentScenario);
                    boolean isAccountChildOfLastElementOfCurrentScenario = isAccountChildOfLastElementOfCurrentScenario(account, currentScenario, witnesses);
                    if (isAccountChildOfLastElementOfCurrentScenario) {
                        currentScenario.add(account);
                    }
                    currentScenario = searchFollowingAccountsInOtherWitnesses(witnesses, indexWitnesses, account, currentScenario);
                }
            }
            // currentScenario.forEach(s -> print(s));
            scenarios.add(currentScenario);
            currentWitnessIndex++;
        }
        for (List<String> scenario : scenarios) {
            for (String s : scenario) {
                print(s);
            }
            print("-----");
        }
    }

    // MISC
    private static List<String[]> addArraysToList(String[] ...lists) {
        List<String[]> resultList = new ArrayList<>();
        if (lists == null && lists.length == 0) {
            return resultList;
        }
        for (String[] list : lists) {
            resultList.add(list);
        }
        return resultList;
    }

    private static List<LinkedList<String>> addListOfArraysToListOfLinkedLists(List<String[]> listOfArrays) {
        List<LinkedList<String>> listOfLinkedLists = new ArrayList<LinkedList<String>>();
        for (String[] listOfArray : listOfArrays) {
            LinkedList<String> currLinkedList = new LinkedList<String>();
            for (int i = listOfArray.length - 1; i > -1; i--) {
                currLinkedList.push(listOfArray[i]);
            }
            listOfLinkedLists.add(currLinkedList);
        }
        return listOfLinkedLists;
    }

    private static List<String> searchLeadingAccountInOtherWitnesses(List<LinkedList<String>> witnesses, int i, String account, List<String> currentScenario) {
        List<String> leadingStory = new ArrayList<>();
        for (int index = 0; index < witnesses.size(); index++) {
            // do not check currentWitness
            if (index == i) {
                continue;
            }
            LinkedList<String> nextWitness = witnesses.get(index);
            int indexOfAccount = nextWitness.indexOf(account);
            if (indexOfAccount > 0) { // account is found and not in the first
                if (currentScenario == null || currentScenario.isEmpty()) {
                    // TODO: add all elements up to indexOfChild to the leadingStory
                    currentScenario.addAll(nextWitness.subList(0, indexOfAccount));
                } else {
                    // TODO: if previous arguments are the child of the last element of the current scenario, then add them to the leading story
                    int indexOfLastElementOfCurrentScenarioInNextWitness = nextWitness.indexOf(currentScenario.get(currentScenario.size()-1));
                    if (indexOfLastElementOfCurrentScenarioInNextWitness > -1 && indexOfLastElementOfCurrentScenarioInNextWitness < nextWitness.size() - 1) {
                        currentScenario.addAll(nextWitness.subList(indexOfLastElementOfCurrentScenarioInNextWitness + 1, indexOfAccount));
                    }
                }
            }
        }
        return currentScenario;
    }

    private static boolean isAccountChildOfLastElementOfCurrentScenario(String account, List<String> currentScenario, List<LinkedList<String>> witnesses) {
        boolean result = false;
        if (currentScenario == null || currentScenario.isEmpty()) {
            return true;
        }
        String lastAccountOfCurrentScenario = currentScenario.get(currentScenario.size() - 1);
        for (LinkedList<String> witness : witnesses) {
            int indexOfLastAccountOfCurrentScenarioInWitness = witness.indexOf(lastAccountOfCurrentScenario);
            if (indexOfLastAccountOfCurrentScenarioInWitness > -1 && indexOfLastAccountOfCurrentScenarioInWitness < witness.size() - 1) {
                if (account.equalsIgnoreCase(witness.get(indexOfLastAccountOfCurrentScenarioInWitness+1))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<String> searchFollowingAccountsInOtherWitnesses(List<LinkedList<String>> witnesses, int i, String account, List<String> currentScenario) {
        List<String> followingAccounts = new ArrayList<>();
        followingAccounts.addAll(currentScenario);
        for (int index = 0; index < witnesses.size(); index++) {
            // do not check currentWitness
            if (index == i) {
                continue;
            }
            LinkedList<String> nextWitness = witnesses.get(index);
            int indexOfAccount = nextWitness.indexOf(account);
            if (indexOfAccount > -1 && indexOfAccount < nextWitness.size() -1) { // if account is found and not the last element (has children)
                List<String> followingAccountsFromNextWitness = nextWitness.subList(indexOfAccount + 1, nextWitness.size());
                if (!currentScenario.containsAll(followingAccountsFromNextWitness) && isFollowingAccountsFromNextWitnessSubListChildOfLastElementOfCurrentScenario(followingAccountsFromNextWitness, currentScenario, witnesses)) {
                    LinkedList<String> currentWitness = witnesses.get(i);
                    // TODO: Warning
                    if (Collections.indexOfSubList(currentWitness, followingAccountsFromNextWitness) == -1) {
                        followingAccounts.addAll(followingAccountsFromNextWitness);
                    }
                }
            }
        }
        return followingAccounts;
    }

    private static boolean isFollowingAccountsFromNextWitnessSubListChildOfLastElementOfCurrentScenario(List<String> followingAccountsFromNextWitness, List<String> currentScenario, List<LinkedList<String>> witnesses) {
        if (currentScenario == null || currentScenario.isEmpty()) {
            return true;
        }
        String lastElement = currentScenario.get(currentScenario.size() - 1);
        List<String> tempFollowingAccountsList = new ArrayList<>(followingAccountsFromNextWitness);
        tempFollowingAccountsList.add(0, lastElement);
        for (LinkedList<String> witness : witnesses) {
            if (Collections.indexOfSubList(witness, tempFollowingAccountsList) > -1) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAnyAccountLeft(List<LinkedList<String>> witnesses, List<List<String>> scenarios) {
        for (LinkedList<String> witness : witnesses) {
            for (String account : witness) {
                if (!isAccountInScenarios(account, scenarios)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAccountInScenarios(String account, List<List<String>> scenarios) {
        for (List<String> scenario : scenarios) {
            if (scenario.contains(account)) {
                return true;
            }
        }
        return false;
    }

    private static void print(Object o) {
        System.out.println(o);
    }

}