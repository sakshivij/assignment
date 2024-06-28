package com.root;
import static net.bytebuddy.matcher.ElementMatchers.*;

import java.lang.instrument.Instrumentation;

import com.root.Advice.AdviceAgents.ApiRecordAdvice;
import com.root.Advice.AdviceAgents.ApiReplayAdvice;
import com.root.Advice.AdviceAgents.DatabaseRecordAdvice;
import com.root.Advice.AdviceAgents.DatabaseReplayAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

class Agent {

  public static void premain(String agentArgs, Instrumentation inst) {


        String[] args = agentArgs.split(",");
        String envVarValue = null;
        for (String arg : args) {
            if (arg.startsWith("HT_MODE=")) {
                envVarValue = arg.substring("HT_MODE=".length());
                break;
            }
        }
        // Use the environment variable value as needed
        System.out.println("Mode: " + envVarValue);
    
        if(envVarValue.equals("Record")){

            new AgentBuilder.Default()
            .disableClassFormatChanges()
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
            .ignore(none())
            .type(ElementMatchers.nameStartsWith("com.root.service"))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
                builder = builder.visit(Advice
                        .to(ApiRecordAdvice.class)
                        .on(ElementMatchers.named("makeApiCall"))
                );
                builder = builder.visit(Advice
                        .to(DatabaseRecordAdvice.class)
                        .on(ElementMatchers.named("saveBlogPost"))
                );
                return builder;
            })
            .installOn(inst);            
        }
        else{
            new AgentBuilder.Default()
            .disableClassFormatChanges()
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
            .ignore(none())
            .type(ElementMatchers.nameStartsWith("com.root.service"))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
                builder = builder.visit(Advice
                        .to(ApiReplayAdvice.class)
                        .on(ElementMatchers.named("makeApiCall"))
                );
                builder = builder.visit(Advice
                        .to(DatabaseReplayAdvice.class)
                        .on(ElementMatchers.named("saveBlogPost"))
                );
                return builder;
            })
            .installOn(inst);            

        }

  }
}