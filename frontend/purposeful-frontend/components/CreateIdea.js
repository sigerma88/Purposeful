import { Fragment, useEffect, useState } from "react";
import {
  Box,
  FormControl,
  FormLabel,
  Input,
  Stack,
  HStack,
  Checkbox,
  Textarea,
  Button,
  useColorModeValue,
  FormErrorMessage,
  Select,
  IconButton,
} from "@chakra-ui/react";
import { Field, Form, Formik } from "formik";
import { getDomains, getTechs, getTopics } from "@/utils/idea_tool";
import ContainerLabel from "./ContainerLabel";
import { RxPlus } from "react-icons/rx";

var fullfilled = 0;
var field_name = "domains";
var c_domains = [];
var c_topics = [];
var c_techs = [];

var domains_sel = <Select id="domains"></Select>;
var topics_sel = <Select id="topics"></Select>;
var techs_sel = <Select id="techs"></Select>;

var rendered_domains = [];
var rendered_topics = [];
var rendered_techs = [];

export default function CreateIdea() {
  const [render_domains, set_rd] = useState(<Fragment></Fragment>);
  const [render_topics, set_tp] = useState(<Fragment></Fragment>);
  const [render_techs, set_tc] = useState(<Fragment></Fragment>);

  var domainContainer = (
    <Stack
      on
      direction={["column", "row"]}
      id={"domainContainer"}
      wrap={"wrap"}
    >
      {render_domains}
    </Stack>
  );
  var topicContainer = (
    <Stack on direction={["column", "row"]} id={"topicContainer"} wrap={"wrap"}>
      {render_topics}
    </Stack>
  );
  var techContainer = (
    <Stack on direction={["column", "row"]} id={"techContainer"} wrap={"wrap"}>
      {render_techs}
    </Stack>
  );
  var domains = [];
  var topics = [];
  var techs = [];

  useEffect(() => {
    getDomains().then((res) => {
      if (fullfilled == 0) {
        fullfilled++;
        domains = res;
        domains.map(MakeOption);
      }
    });
    getTopics().then((res) => {
      if (fullfilled == 1) {
        fullfilled++;
        topics = res;
        field_name = "topics";
        topics.map(MakeOption);
      }
    });
    getTechs().then((res) => {
      if (fullfilled == 2) {
        fullfilled++;
        techs = res;
        field_name = "techs";
        techs.map(MakeOption);
      }
    });
  }, []);

  var refreshfn = function () {
    set_rd(<Fragment>{rendered_domains.concat([])}</Fragment>);
    set_tp(<Fragment>{rendered_topics.concat([])}</Fragment>);
    set_tc(<Fragment>{rendered_techs.concat([])}</Fragment>);
  };
  function MakeOption(X) {
    const el = document.createElement("option");
    el.setAttribute("value", X.id);
    el.textContent = X.name;
    document.getElementById(field_name).appendChild(el);
  }

  function PushObj(selectedIndex, arr, c_arr, arr2) {
    if (find_in_arr(selectedIndex, arr, c_arr) == -1) {
      const el = (
        <ContainerLabel
          key={arr[selectedIndex].name}
          innerTxt={arr[selectedIndex].name}
          arr={c_arr}
          arr2={arr2}
          refresh={refreshfn}
        />
      );
      c_arr.push(arr[selectedIndex]);
      arr2.push(el);
      refreshfn();
    }
  }
  return (
    <Stack width={"75%"}>
      <Box
        //width={"1"}
        rounded={"lg"}
        bg={useColorModeValue("white", "gray.700")}
        boxShadow={"lg"}
        p={8}
      >
        <Formik
          initialValues={{
            title: "",
            purpose: "",
            description: "",
          }}
          onSubmit={(values, actions) => {
            setTimeout(async () => {
              console.log(values); // TODO: To be removed once the API is connected
              // TODO: Set the error messages for the fields according to the API response
              // TODO: Differentiate API methods depending on authentication status
              actions.setSubmitting(false);
              // TODO: Redirect to the login page
            }, 1000);
          }}
        >
          {(props) => (
            <Form>
              <HStack spacing={2}>
                <Stack spacing={4} width={"50%"}>
                  <Box>
                    <Field name="title">
                      {({ field, form }) => (
                        <FormControl>
                          <FormLabel>Title</FormLabel>
                          <Input placeholder="Title" {...field} type="text" />
                        </FormControl>
                      )}
                    </Field>
                  </Box>
                  <Box>
                    <Field name="purpose">
                      {({ field, form }) => (
                        <FormControl id="purpose">
                          <FormLabel>Purpose</FormLabel>
                          <Input placeholder="Purpose" {...field} type="text" />
                        </FormControl>
                      )}
                    </Field>
                  </Box>
                  <Box>
                    <Field name="description">
                      {({ field, form }) => (
                        <FormControl id="description">
                          <FormLabel>Description</FormLabel>
                          <Textarea
                            placeholder="Briefly describe your idea here..."
                            {...field}
                            rows={"10"}
                          />
                        </FormControl>
                      )}
                    </Field>
                  </Box>
                  <Field name="iconurl">
                    {({ field, form }) => (
                      <FormControl id="iconurl">
                        <FormLabel>Icon URL</FormLabel>
                        <Input
                          placeholder="https://picsum.photos/200"
                          {...field}
                          type="text"
                        />
                      </FormControl>
                    )}
                  </Field>
                  <Box></Box>
                </Stack>
                <Stack spacing={4} width={"50%"} padding={"50px"}>
                  <Checkbox name="ispaid">Paid</Checkbox>
                  <Checkbox name="inprogress">In Progress</Checkbox>
                  <Checkbox name="isprivate">Private</Checkbox>
                  <Box>
                    <Field name="Domain">
                      {({ field, form }) => (
                        <FormControl id="domain">
                          <FormLabel>Domains</FormLabel>
                          <HStack>
                            {domains_sel}
                            <IconButton
                              icon={<RxPlus />}
                              borderRadius={"20px"}
                              onClick={() =>
                                PushObj(
                                  document.getElementById("domains")
                                    .selectedIndex,
                                  domains,
                                  c_domains,
                                  rendered_domains
                                )
                              }
                            />
                          </HStack>
                        </FormControl>
                      )}
                    </Field>
                    {domainContainer}
                  </Box>
                  <Box>
                    <Field name="Topic">
                      {({ field, form }) => (
                        <FormControl
                          id="topic"
                          isInvalid={form.errors.topic && form.touched.topic}
                        >
                          <FormLabel>Topics</FormLabel>
                          <HStack>
                            {topics_sel}
                            <IconButton
                              icon={<RxPlus />}
                              borderRadius={"20px"}
                              onClick={() =>
                                PushObj(
                                  document.getElementById("topics")
                                    .selectedIndex,
                                  topics,
                                  c_topics,
                                  rendered_topics
                                )
                              }
                            />
                          </HStack>
                        </FormControl>
                      )}
                    </Field>
                    {topicContainer}
                  </Box>
                  <Box>
                    <Field name="Tech">
                      {({ field, form }) => (
                        <FormControl
                          id="tech"
                          isInvalid={form.errors.tech && form.touched.tech}
                        >
                          <FormLabel>Technologies</FormLabel>
                          <HStack>
                            {techs_sel}
                            <IconButton
                              icon={<RxPlus />}
                              borderRadius={"20px"}
                              onClick={() =>
                                PushObj(
                                  document.getElementById("techs")
                                    .selectedIndex,
                                  techs,
                                  c_techs,
                                  rendered_techs
                                )
                              }
                            />
                          </HStack>
                        </FormControl>
                      )}
                    </Field>
                    {techContainer}
                  </Box>
                  <Box>
                    <Field name="supportingimgurl">
                      {({ field, form }) => (
                        <FormControl id="supportingimgurls">
                          <FormLabel>Supporting Images (Optional)</FormLabel>
                          <Input
                            marginTop={"5px"}
                            placeholder="URL 1 (Optional)"
                            {...field}
                            type="text"
                          />
                          <Input
                            marginTop={"10px"}
                            placeholder="URL 2 (Optional)"
                            {...field}
                            type="text"
                          />
                          <Input
                            marginTop={"10px"}
                            placeholder="URL 3 (Optional)"
                            {...field}
                            type="text"
                          />
                        </FormControl>
                      )}
                    </Field>
                  </Box>
                </Stack>
              </HStack>
              <Box width={"100%"} display={"flex"} justifyContent={"center"}>
                <Button
                  width={"30%"}
                  bg={"blue.400"}
                  color={"white"}
                  _hover={{
                    bg: "blue.500",
                  }}
                  type="submit"
                >
                  Create Idea
                </Button>
              </Box>
            </Form>
          )}
        </Formik>
      </Box>
    </Stack>
  );
}

export function find_in_arr(index, arr, c_arr) {
  for (var i = 0; i < c_arr.length; i++) {
    if (c_arr[i].id === arr[index].id) {
      return i;
    }
  }
  return -1;
}
