package com.root;
import static net.bytebuddy.matcher.ElementMatchers.*;

import java.lang.instrument.Instrumentation;

import com.root.Advice.AdviceAgents.ApiRecordAdvice;
import com.root.Advice.AdviceAgents.DatabaseRecordAdvice;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

class Agent {

//   public static class ToStringAdvice {
//     // @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
//     // public static boolean before() {
//     //     // Skip original method execution (false is the default value for boolean)
//     //     //throw new IllegalArgumentException("Input is empty");
//     //     return false;
//     // }

//     @Advice.OnMethodExit
//     public static void after(@Advice.Return(readOnly = false) Long returnValue) {
//         // Set fixed return value
//         System.out.println("Byte buddy Intercept. Return response" + returnValue);
//     }

//     // @Advice.OnMethodExit
//     // public static void after(@Advice.Return(readOnly = false) String returnValue) {
//     //     // Set fixed return value
//     //     returnValue = "HELLO BYTE BUDDY!";
//     // }
// }

  public static void premain(String agentArgs, Instrumentation inst) {

        // AgentBuilder agentBuilder = new AgentBuilder.Default()
        //         .disableClassFormatChanges()
        //         .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
        //         .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
        //         .ignore(none());

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
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                    builder.visit(Advice
                            .to(ApiRecordAdvice.class)
                            .on(ElementMatchers.named("makeApiCall"))
                    )
                ).type(ElementMatchers.nameStartsWith("com.root.service"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                builder.visit(Advice
                        .to(DatabaseRecordAdvice.class)
                        .on(ElementMatchers.named("saveBlogPost"))
                )
                ).installOn(inst);
            
            // agentBuilder.type(ElementMatchers.nameStartsWith("com.root.service"))
            // .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
            //         builder.visit(Advice
            //                 .to(DatabaseRecordAdvice.class)
            //                 .on(ElementMatchers.named("saveBlogPost"))
            //         )
            // ); 
        }
        // else{

        //     agentBuilder.type(ElementMatchers.nameStartsWith("com.root.service"))
        //     .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
        //             builder.visit(Advice
        //                     .to(ApiReplayAdvice.class)
        //                     .on(ElementMatchers.named("makeApiCall"))
        //             )
        //     ); 
            
        //     agentBuilder.type(ElementMatchers.nameStartsWith("com.root.service"))
        //     .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
        //             builder.visit(Advice
        //                     .to(DatabaseReplayAdvice.class)
        //                     .on(ElementMatchers.named("saveBlogPost"))
        //             )
        //     ); 

        // }

//        agentBuilder.installOn(inst);

  }
}