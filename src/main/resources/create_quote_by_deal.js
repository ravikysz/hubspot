const hubspot = require('@hubspot/api-client');
const request = require('request');

exports.main = async (event, callback) => {

  /*****
    How to use secrets
    Secrets are a way for you to save API keys or private apps and set them as a variable to use anywhere in your code
    Each secret needs to be defined like the example below
  *****/

  const hubspotClient = new hubspot.Client({
    accessToken: "pat-na1-230fe4e2-eab8-4734-a572-ff51c3e77492"
  })

  let amount;
  let dealName;
  let dealId;
  let currencyCode;

  const dealProperties = [
    "deal_currency_code",
    "dealname",
    "amount"
  ];
  const propertiesWithHistory = [];
  const associations = undefined;
  const archived = false;
  const idProperty = undefined;

  try {
    const ApiResponse = await hubspotClient.crm.deals.basicApi.getById(event.object.objectId, dealProperties, propertiesWithHistory, associations, archived, idProperty);
    amount = ApiResponse.body.properties.amount;
    dealName = ApiResponse.body.properties.dealname;
    dealId = ApiResponse.body.id;
    currencyCode = ApiResponse.body.properties.deal_currency_code;
  } catch (err) {
    console.error(err);
    throw err;
  }

  const adjustedTimeAsMs = Date.now() + (1000 * 60 * 60 * 24 * 15);

  // Create Quote
  const properties = {
    hs_title: dealName + " - Quote",
    hs_expiration_date: adjustedTimeAsMs,
    hs_currency: currencyCode
  };

  const quoteObj = { properties };

  const options = {
    headers: {
      'Authorization': 'Bearer pat-na1-230fe4e2-eab8-4734-a572-ff51c3e77492',
      'Content-Type': 'application/json'
    },
    url: 'https://api.hubapi.com/crm/v3/objects/quotes',
    json: true,
    body: quoteObj
  };

  function createQuote() {
    return new Promise((resolve, reject) => {
      request.post(options, function(err, res, body) {
        if (err) reject(err);
        resolve(body);
      });
    });
  }

  const quoteRes = await createQuote().catch(err => console.log(err));

  // Update Quote associate with deal deal_to_quote
  try {
    await hubspotClient.crm.deals.associationsApi.create(dealId, "quote", quoteRes.id, "deal_to_quote");
  } catch (e) {
    e.message === 'HTTP request failed'
      ? console.error('success')
    : console.error(e)
  }

  // Update Quote associate with template quote_to_quote_template
  const template_options = {
    headers: {
      'Authorization': 'Bearer pat-na1-230fe4e2-eab8-4734-a572-ff51c3e77492',
      'Content-Type': 'application/json'
    },
    url: 'https://api.hubapi.com/crm/v3/objects/quote/' + quoteRes.id + '/associations/quote_template/165154431339/quote_to_quote_template',
    json: true
  };

  request.put(template_options, (err, resp, body) => {
    if (err) console.log(err);
    console.log(body.id);
  });

  // Create Line Items
  const lineItemObject = {
    name: "1 year implementation consultation",
    quantity: 1,
    price: amount
  };

  let lineItemId;
  try {
    const apiResponse = await hubspotClient.crm.lineItems.basicApi.create({properties: lineItemObject});
    lineItemId = apiResponse.body.id;
  } catch (e) {
    e.message === 'HTTP request failed'
      ? console.error(JSON.stringify(e.response, null, 2))
      : console.error(e)
  }

  // Update Deal associate with Product line_item_to_deal
  try {
    await hubspotClient.crm.lineItems.associationsApi.create(lineItemId, "deal", dealId, "line_item_to_deal");
  } catch (e) {
    e.message === 'HTTP request failed'
      ? console.error(JSON.stringify(e.response, null, 2))
      : console.error(e)
  }

  // Update Quote associate with Product line_item_to_quote
  try {
    await hubspotClient.crm.lineItems.associationsApi.create(lineItemId, "quote", quoteRes.id, "line_item_to_quote");
  } catch (e) {
    e.message === 'HTTP request failed'
      ? console.error(JSON.stringify(e.response, null, 2))
      : console.error(e)
  }

  // Update quote status with approved
  const updateQuoteOptions = {
    headers: {
      'Authorization': 'Bearer pat-na1-230fe4e2-eab8-4734-a572-ff51c3e77492',
      'Content-Type': 'application/json'
    },
    url: 'https://api.hubapi.com/crm/v3/objects/quote/' + quoteRes.id,
    json: true,
    body: { properties : { hs_status : "APPROVAL_NOT_NEEDED" }}
  };

  function updateQuote() {
    return new Promise((resolve, reject) => {
      request.patch(updateQuoteOptions, function(err, respo, body) {
        if (err) reject(err);
        resolve(body);
      });
    });
  }

  await updateQuote().catch(err => console.log(err));

  /*****
    How to use inputs
    Inputs are a way for you to take data from any actions in your workflow and use it in your code instead of having to call the HubSpot API to get that same data.
    Each input needs to be defined like the example below
  *****/

  // const email = event.inputFields['email'];


  /*****
    How to use outputs
    Outputs are a way for you to take data from your code and use it in later workflows actions
   Use the callback function to return data that can be used in later actions.
    Data won't be returned until after the event loop is empty, so any code after this will still execute.
  *****/

  callback({
    outputFields: {
      amount: amount,
      dealName: dealName
    }
  });
}

/* A sample event may look like:
{
  "origin": {
    // Your portal ID
    "portalId": 1,

    // Your custom action definition ID
    "actionDefinitionId": 2,
  },
  "object": {
    // The type of CRM object that is enrolled in the workflow
    "objectType": "CONTACT",

    // The ID of the CRM object that is enrolled in the workflow
    "objectId": 4,
  },
  "inputFields": {
    // The property name for defined inputs
  },
  // A unique ID for this execution
  "callbackId": "ap-123-456-7-8"
}
*/
