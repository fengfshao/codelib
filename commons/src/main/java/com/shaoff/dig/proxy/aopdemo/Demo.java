package com.shaoff.dig.proxy.aopdemo;


public class Demo {

  public static void main(String[] args) {
    ServiceA a = CGLibContainer.getInstance(ServiceA.class);
    a.callB();
  }
}
