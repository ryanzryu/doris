-- SELECT cast(payload["comment"]["body"] as string) as comment, count() FROM github_events WHERE cast(payload["comment"]["body"] as string) != "" AND length(cast(payload["comment"]["body"] as string)) < 100 GROUP BY comment  ORDER BY count() DESC, comment, 1  LIMIT 1