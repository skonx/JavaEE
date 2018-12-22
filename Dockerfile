FROM payara/micro
ADD ./target/webapp1-12.1.war $PAYARA_PATH
ENTRYPOINT ["java", "-jar", "$PAYARA_PATH/payara-micro.jar", "--deploy", "$PAYARA_PATH/webapp1-12.1.war"]
