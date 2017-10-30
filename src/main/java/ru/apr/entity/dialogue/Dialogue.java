package ru.apr.entity.dialogue;

import org.springframework.stereotype.Component;
import ru.apr.entity.MallRatBot;

import java.util.Map;

@Component
public class Dialogue {
    private Integer currentStep = 0;
    private Integer userId = 0;
    private Map<Integer, String> stepText;
    private Map<Integer, Integer> stepMap;
    private Map<Integer, String> callbackMap;
    private String dialogueType;

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public Map<Integer, String> getStepText() {
        return stepText;
    }

    public void setStepText(Map<Integer, String> stepText) {
        this.stepText = stepText;
    }

    public Map<Integer, Integer> getStepMap() {
        return stepMap;
    }

    public void setStepMap(Map<Integer, Integer> stepMap) {
        this.stepMap = stepMap;
    }

    public Map<Integer, String> getCallbackMap() {
        return callbackMap;
    }

    public void setCallbackMap(Map<Integer, String> callbackMap) {
        this.callbackMap = callbackMap;
    }


    public String proceedConversation() {
        String messageText = stepText.get(currentStep);

        // научить выполнять действие в зависимости от типа

        if(currentStep == -1){
            endDialogue();
        }
        else{
            currentStep = stepMap.get(currentStep);
        }

        return messageText;
    }

    protected void endDialogue(){
        MallRatBot.getConversations().remove(userId);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDialogueType() {
        return dialogueType;
    }

    public void setDialogueType(String dialogueType) {
        this.dialogueType = dialogueType;
    }

}
