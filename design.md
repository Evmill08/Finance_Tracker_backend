1. User Logs in:
    - Enters account information with Plaid API stuff
    - Must enter ALL bank account, so if they have more than one bank, they must enter all banks

2. Home Page:
    - Shows break down of all spending and income for each month
    - Pie chart of expenses broken down into different categories
    - Below that, there is a list of individual payments and income (probably split this or like a timeline kinda)

3. Savings Page:
    - Two Options:
        - Set a simple monthly savings goal
        - Set a retirement age and autocalculate the savings goal
    - Savings goal can be changed or recalculated at any time
    - User needs to input their expected monthly income if they have not had the app for more than 2 weeks (paycheck period we can use to estimate their monthly income)
    - Shows a monthly savings progress bar, in the middle is the amount needed to save for each paycheck
    - Possible AI integration here to analyze spending and determine how they can meet their savings goals

4. Long Term Page: 
    - Shows the overall spend vs earned vs saved
    - Analyzes patterns

5. Wealth Management Information:
    - A collection of resources to learn more about wealth management
    - Investments, savings strategies, etc

6. Monthly Sumamry:
    - Not a page, just something that is triggered on the first launch of a new month
    - Shows the amount spent, earned, and saved.
    - Checks if the savings limit was reached, if so, congratulates, otherwise, offers suggesstions
    - Shows what they spent the most on (item and category)


Endpoints of Interest:
    - transactions/sync: retrieves transactions associated with an Item and can fetch updates using a cursor to track which updates have already been seen.
    - transactions/recurring: allows developers to receive a summary of the recurring outflow and inflow streams (expenses and deposits) from a user’s checking, savings or credit card accounts
    - investments/holdings/get: allows developers to receive user-authorized stock position data for investment-type accounts.
    - investments/transactions/get: allows developers to retrieve up to 24 months of user-authorized transaction data for investment accounts.