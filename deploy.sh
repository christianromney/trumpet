#!/bin/bash
aws s3 sync out s3://christianromney.org/trumpet/ --acl public-read --profile kip
