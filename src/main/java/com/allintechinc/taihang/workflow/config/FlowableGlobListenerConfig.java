package com.allintechinc.taihang.workflow.config;


import com.allintechinc.taihang.workflow.listener.GlobalOperatorListener;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author sundp
 */
@Configuration
public class FlowableGlobListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SpringProcessEngineConfiguration configuration;
    @Autowired
    private GlobalOperatorListener globalOperatorListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        //流程结束全局监听
        dispatcher.addEventListener(globalOperatorListener, FlowableEngineEventType.TASK_CREATED);
        dispatcher.addEventListener(globalOperatorListener, FlowableEngineEventType.TASK_COMPLETED);
        dispatcher.addEventListener(globalOperatorListener, FlowableEngineEventType.PROCESS_COMPLETED);
        dispatcher.addEventListener(globalOperatorListener, FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT);
    }
}
