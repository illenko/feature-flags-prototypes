```shell
docker run \
  -p 1031:1031 \
  -v $(pwd)/flags.yaml:/goff/flag-config.yaml \
  -v $(pwd)/goff-proxy-local.yaml:/goff/goff-proxy.yaml \
  gofeatureflag/go-feature-flag:latest
```