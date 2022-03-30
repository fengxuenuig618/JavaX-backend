package com.fengxue.javax_backend.util;

import java.util.*;

public enum McqStateMachine {
    PreRight("PR"),
    PreWrong("PW"),
    BeforeRight("BR"),
    LearnRight("LR"),
    OccasionalRight("OR"),
    LearnWrong("LW"),
    OccasionalLearnWrong("OLW"),
    OccasionalBeforeWrong("OBW"),
    MultipleWrong("MW")
    ;

    private final String value;
    McqStateMachine(String value) {
        this.value=value;
    }

    public String getValue(){
        return this.value;
    }

    public static String getNextRight(String lastState){
        String nextState = "";
        switch (lastState){
            case "PR": nextState = "BR";break;
            case "PW": nextState = "OR";break;
            case "BR": nextState = "BR";break;
            case "LR": nextState = "LR";break;
            case "OR": nextState = "LR";break;
            case "LW": nextState = "OR";break;
            case "OLW": nextState = "LR";break;
            case "OBW": nextState = "BR";break;
            case "MW": nextState = "OR";break;
        }

        return nextState;

    }

    public static String getNextWrong(String lastState){
        String nextState = "";
        switch (lastState){
            case "PR": nextState = "LW";break;
            case "PW": nextState = "LW";break;
            case "BR": nextState = "OBW";break;
            case "LR": nextState = "OLW";break;
            case "OR": nextState = "LW";break;
            case "LW": nextState = "MW";break;
            case "OLW": nextState = "LW";break;
            case "OBW": nextState = "LW";break;
            case "MW": nextState = "MW";break;
        }

        return nextState;
    }

    public static Set<String> getWrongSet(){
        Set<String> retSet = new HashSet<>();
        retSet.add(McqStateMachine.PreWrong.value);
        retSet.add(McqStateMachine.LearnWrong.value);
        retSet.add(McqStateMachine.OccasionalLearnWrong.value);
        retSet.add(McqStateMachine.OccasionalBeforeWrong.value);
        retSet.add(McqStateMachine.MultipleWrong.value);
        return retSet;
    }

    public static Set<String> getCorrectSet(){
        Set<String> retSet = new HashSet<>();
        retSet.add(McqStateMachine.PreRight.value);
        retSet.add(McqStateMachine.BeforeRight.value);
        retSet.add(McqStateMachine.LearnRight.value);
        retSet.add(McqStateMachine.OccasionalRight.value);
        return retSet;
    }

    public static Set<String> getUncertainSet(){
        Set<String> retSet = new HashSet<>();
        retSet.add(McqStateMachine.PreWrong.value);
        retSet.add(McqStateMachine.LearnWrong.value);
        retSet.add(McqStateMachine.OccasionalLearnWrong.value);
        retSet.add(McqStateMachine.OccasionalBeforeWrong.value);
        retSet.add(McqStateMachine.MultipleWrong.value);
        retSet.add(McqStateMachine.OccasionalRight.value);
        return retSet;
    }

    public static Set<String> getALLSet(){
        Set<String> retSet = new HashSet<>();
        retSet.add(McqStateMachine.PreWrong.value);
        retSet.add(McqStateMachine.LearnWrong.value);
        retSet.add(McqStateMachine.OccasionalLearnWrong.value);
        retSet.add(McqStateMachine.OccasionalBeforeWrong.value);
        retSet.add(McqStateMachine.MultipleWrong.value);
        retSet.add(McqStateMachine.PreRight.value);
        retSet.add(McqStateMachine.BeforeRight.value);
        retSet.add(McqStateMachine.LearnRight.value);
        retSet.add(McqStateMachine.OccasionalRight.value);
        return retSet;
    }

    public static String getScore(Map<String,String> dataMap){
        Map<String,Integer> stateMap = new HashMap<>();
        for(String state:McqStateMachine.getALLSet()){
            stateMap.put(state,0);
        }
        for(String key:dataMap.keySet()){
            String curState = dataMap.get(key);
            stateMap.put(curState,stateMap.get(curState)+1);
        }
        int all = dataMap.size();
        int each = 100/all;
        int score = 100;
        score -= stateMap.get(McqStateMachine.MultipleWrong.value)*each*2;
        score -= stateMap.get(McqStateMachine.LearnWrong.value)*each;
        score -= stateMap.get(McqStateMachine.OccasionalLearnWrong.value)*each*0.5;
        score -= stateMap.get(McqStateMachine.OccasionalBeforeWrong.value)*each*0.5;
        score = Math.max(score, 0);
        return String.valueOf(score);
    }

    public static List<String> getAssessment(Map<String,String> dataMap, String name){
        List<String> retString = new ArrayList<>();

        List<String> perfect = new ArrayList<>();
        List<String> good = new ArrayList<>();
        List<String> mediocre = new ArrayList<>();
        List<String> bad = new ArrayList<>();

        for(String key:dataMap.keySet()){
            String value = dataMap.get(key);
            int score = Integer.parseInt(value);
            if(score==100){
                perfect.add(key);
            }
            else if(score>=70){
                good.add(key);
            }
            else if(score>=40){
                mediocre.add(key);
            }
            else{
                bad.add(key);
            }
        }

        StringBuilder perfectAssessment = new StringBuilder();
        if(perfect.size()>0){
            perfectAssessment.append("You have a perfect grasp of ");
            perfectAssessment.append(name);
            perfectAssessment.append(" ( ");
            for(String xx:perfect){
                perfectAssessment.append(xx);
                perfectAssessment.append(" ");
            }
            perfectAssessment.append(")\n");
            retString.add(perfectAssessment.toString());
        }

        StringBuilder goodAssessment = new StringBuilder();
        if(good.size()>0){
            goodAssessment.append("In ");
            goodAssessment.append(name);
            goodAssessment.append(" ( ");
            for(String xx:good){
                goodAssessment.append(xx);
                goodAssessment.append(" ");
            }
            goodAssessment.append("), you learned well, with only few mistakes.\n");
            retString.add(goodAssessment.toString());
        }

        StringBuilder mediocreAssessment = new StringBuilder();
        if(mediocre.size()>0){
            mediocreAssessment.append("You got a lot of questions wrong in ");
            mediocreAssessment.append(name);
            mediocreAssessment.append(" ( ");
            for(String xx:mediocre){
                mediocreAssessment.append(xx);
                mediocreAssessment.append(" ");
            }
            mediocreAssessment.append("), you can go to the \"My Wrong questions\" section to practice again.\n");
            retString.add(mediocreAssessment.toString());
        }

        StringBuilder badAssessment = new StringBuilder();
        if(bad.size()>0){
            badAssessment.append("You did not learn ");
            badAssessment.append(name);
            badAssessment.append(" ( ");
            for(String xx:bad){
                badAssessment.append(xx);
                badAssessment.append(" ");
            }
            badAssessment.append(") well. Please restudy and complete the quiz.\n");
            retString.add(badAssessment.toString());
        }
        return retString;
    }

}
