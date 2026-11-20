.PHONY: start stop restart logs status test

start:
	docker compose up --build -d

stop:
	docker compose down

restart: stop start

logs:
	docker compose logs -f app

status:
	docker compose ps

test:
	./gradlew test
