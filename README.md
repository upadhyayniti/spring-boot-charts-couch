# spring-boot-charts-couch



### Setting pipeline

Install Red Hat OpenShift Pipelines Operator on OpenShift using defaults

```sh
oc new-project charts
oc apply -f pipeline/00_pipeline_storage.yaml
oc apply -f pipeline/01_apply_manifest_task.yaml
oc apply -f pipeline/02_updated_deployment_task.yaml
oc apply -f pipeline/03_pipeline.yaml

tkn pipeline start charts-pipeline  -w name=shared-workspace,claimName=source-pvc -p NAME=charts -p CODE=https://github.com/upadhyayniti/spring-boot-charts-couch -p BRANCH=mvp     -p IMAGE=image-registry.openshift-image-registry.svc:5000/charts/charts:latest     --use-param-defaults
```
