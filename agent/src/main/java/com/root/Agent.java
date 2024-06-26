package com.root;
import static net.bytebuddy.matcher.ElementMatchers.*;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;

class Agent {

  public static class ToStringAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    public static boolean before() {
        // Skip original method execution (false is the default value for boolean)
        return false;
    }

    @Advice.OnMethodExit
    public static void after(@Advice.Return(readOnly = false) String returnValue) {
        // Set fixed return value
        returnValue = "HELLO BYTE BUDDY!";
    }
}

  public static void premain(String agentArgs, Instrumentation inst) {
    new AgentBuilder.Default()
      .disableClassFormatChanges()
      .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
      .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
      .ignore(none())
      .type(nameStartsWith("com.root"))  // Use named(Object.class.getName()) to be more precise
      .transform((builder, typeDescription, classLoader, module, protectionDomain) -> 
        builder.visit(
          Advice
            .to(ToStringAdvice.class)
            .on(named("toString"))
        )
      )
      .installOn(inst);
  }

  // public static void main(String[] args) {
  //   // This is a standalone test. For a real agent, you should package it as a JAR.
  //   Instrumentation instrumentation = ByteBuddyAgent.install();
  //       premain("", instrumentation);
  //       try {
  //           Class<?> testClass = Class.forName("com.test.TestClass");
  //           testClass.getDeclaredMethod("main", String[].class).invoke(null, (Object) null);
  //       } catch (Exception e) {
  //           e.printStackTrace();
  //       }
  //     }
}
