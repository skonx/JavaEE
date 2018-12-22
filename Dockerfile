FROM payara/micro
COPY ./target/webapp1-12.1.war ${DEPLOYMENT_DIR}
