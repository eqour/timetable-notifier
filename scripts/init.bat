mkdir "./data"
mkdir "./data/google-drive"
mkdir "./data/log"
(
echo {
echo   "maxDelayAfterChange": 5,
echo   "zoneOffset": 4,
echo   "parsingPeriod": 7,
echo   "vkToken": "< your token >",
echo   "telegramToken": "< your token >",
echo   "timetableFileId": "< your file id >",
echo   "dbConnection": "mongodb://localhost:27017"
echo }
) > "./settings.json"