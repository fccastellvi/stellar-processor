# stellar-processor

- Add grafana metrics and dashboard, create registry in docker
- Add CD tool
- Create rest api through http4s--> dockerize it
- Make use of aws products through localstack
- Add terraform to localstack
- Use properly kafka streams
- Try flink
 
 
 ```
 fluxctl install --manifest-generation=true --git-user=fccastellvi --git-email=fcab65@gmail.com --git-url=git@github.com:fccastellvi/stellar-processor.git --git-branch master --git-path=flux/releases/kafka --namespace=flux | kubectl apply -f -
```