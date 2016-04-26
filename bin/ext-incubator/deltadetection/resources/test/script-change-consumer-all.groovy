log.info('input resource (Media code): ' + cronjob.job.input.code)
changeDetectionService.consumeChanges([change])
log.info('Consumed: ' + change)
true
