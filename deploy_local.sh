mvn -DaltDeploymentRepository=snapshot-repo::default::file:../joelso-mvn-repo/snapshots clean deploy;
git --exec-path=../joelso-mvn-repo/ add .;
git --exec-path=../joelso-mvn-repo/ commit -m 'new snapshot: spring-flash';
git --exec-path=../joelso-mvn-repo/ push;

