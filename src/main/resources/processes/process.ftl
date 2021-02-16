<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
             xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
             xmlns:xsd="http://www.w3.org/2001/XMLSchema"
             xmlns:flowable="http://flowable.org/bpmn"
             targetNamespace="http://www.flowable.org/processdef"
             exporter="Camunda Modeler" exporterVersion="3.0.0">
    <process id="${processKey}" name="${processName}" isExecutable="true">
        <startEvent id="startEvent_1" name="Start"/>

        <#list taskList as userTask>
            <userTask id="${userTask.id}" name="${userTask.name}" <#if (userTask.assignee)??>flowable:assignee="${userTask.assignee}"</#if>/>
        </#list>

        <#list gatewayList as gateway>
            <exclusiveGateway id="${gateway.id}" name="${gateway.name}"/>
        </#list>

        <#list flowList as sequenceFlow>
            <sequenceFlow id="${sequenceFlow.id}" name="${sequenceFlow.name}" sourceRef="${sequenceFlow.sourceRef}" targetRef="${sequenceFlow.targetRef}">
                <#if (sequenceFlow.conditionExpression)??>
                    ${sequenceFlow.conditionExpression}
                </#if>
            </sequenceFlow>
        </#list>

        <endEvent id="endEvent_1" name="End"/>
        <endEvent id="terminateEvent_1" name="Terminate">
            <terminateEventDefinition/>
        </endEvent>
    </process>
</definitions>
