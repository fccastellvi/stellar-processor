# stellar-processor

- Add grafana metrics and dashboard, create registry in docker
- Add CD tool
- Create rest api through http4s--> dockerize it
- Make use of aws products through localstack
- Add terraform to localstack
- Use properly kafka streams
- Try flink
 
 
 
```
1- kubectl create ns flux
``` 

```
2- 
brew install helm fluxctl
fluxctl install --manifest-generation=true --git-user=fccastellvi --git-email=fcab65@gmail.com --git-url=git@github.com:fccastellvi/stellar-processor.git --git-branch master --git-path=flux/releases/kafka --namespace=flux | kubectl apply -f -
```
```
3- helm repo add fluxcd https://charts.fluxcd.io
```
```
4- Add deploy keys to github (fluxctl identity --k8s-fwd-ns flux)
```
```
5-
helm upgrade -i helm-operator fluxcd/helm-operator \
      --namespace flux \
      --set git.ssh.secretName=flux \
      --set helm.versions=v3
```